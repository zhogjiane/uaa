/*
 * MIT License
 *
 * Copyright (c) [2021] [sky5486560@gmail.com]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.conifercone.uaa.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conifercone.uaa.domain.entity.SysUser;
import com.conifercone.uaa.domain.entity.SysUserRole;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.SysUserRoleVO;
import com.conifercone.uaa.domain.vo.SysUserVO;
import com.conifercone.uaa.mapper.UserMapper;
import com.conifercone.uaa.service.IUserRoleService;
import com.conifercone.uaa.service.IUserService;
import com.conifercone.uaa.util.DataValidationUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户service实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
@Service
@Slf4j
@Api(value = "用户管理", tags = "用户管理")
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "User:")
    @SuppressWarnings("unused")
    private Cache<Long, SysUserVO> userCache;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "UserRole:")
    @SuppressWarnings("unused")
    private Cache<Long, SysUserRoleVO> userRoleCache;

    @Resource
    Snowflake snowflake;

    @Resource
    IUserRoleService userRoleService;

    /**
     * 新增用户
     *
     * @param newUser 新用户
     * @return {@link SysUserVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserVO newUsers(SysUserVO newUser) {
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysUser::getAccountName, newUser.getAccountName()))) {
            throw new BizException(ResultCode.DUPLICATE_ACCOUNT_NAME, newUser.getAccountName());
        }
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysUser::getPhoneNumber, newUser.getPhoneNumber()))) {
            throw new BizException(ResultCode.DUPLICATE_PHONE_NUMBER, newUser.getPhoneNumber());
        }
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysUser::getEmail, newUser.getEmail()))) {
            throw new BizException(ResultCode.DUPLICATE_EMAIL_ADDRESS, newUser.getEmail());
        }
        //设置用户id
        final long userId = snowflake.nextId();
        newUser.setId(userId);
        //密码加密
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        final SysUser sysUser = BeanUtil.copyProperties(newUser, SysUser.class);
        //保存用户信息
        this.save(sysUser);
        //保存用户角色信息
        saveUserRoleInformation(newUser, userId);
        final SysUserVO sysUserVO = BeanUtil.copyProperties(this.getById(userId), SysUserVO.class);
        //去除密码等敏感信息
        sysUserVO.setPassword("");
        userCache.PUT(sysUserVO.getId(), sysUserVO);
        return sysUserVO;
    }

    private void saveUserRoleInformation(SysUserVO newUser, long userId) {
        final List<Long> roleIds = newUser.getRoleIds();
        if (CollUtil.isNotEmpty(roleIds)) {
            final List<SysUserRole> sysUserRoleList = roleIds.stream().map(roleId -> {
                final long userRoleId = snowflake.nextId();
                final SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(userRoleId);
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);
                return sysUserRole;
            }).collect(Collectors.toList());
            userRoleService.saveBatch(sysUserRoleList);
            final Map<Long, SysUserRoleVO> sysUserRoleVOMap = sysUserRoleList
                    .stream()
                    .map(sysUserRole -> BeanUtil.copyProperties(sysUserRole, SysUserRoleVO.class))
                    .collect(Collectors.toList())
                    .stream()
                    .collect(Collectors.toMap(SysUserRoleVO::getId, sysUserRoleVO -> sysUserRoleVO));
            userRoleCache.PUT_ALL(sysUserRoleVOMap);
        }
    }

    /**
     * 删除用户
     *
     * @param sysUserIdList 系统用户id列表
     * @return {@link List}<{@link SysUserVO}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysUserVO> deleteUsers(List<Long> sysUserIdList) {
        final List<SysUser> sysUsers = this.listByIds(sysUserIdList);
        this.removeByIds(sysUserIdList);
        userCache.REMOVE_ALL(new HashSet<>(sysUserIdList));
        return Optional.ofNullable(sysUsers).orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysUser -> BeanUtil.copyProperties(sysUser, SysUserVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 修改用户
     *
     * @param newUser 新用户
     * @return {@link SysUserVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserVO modifyUser(SysUserVO newUser) {
        this.updateById(BeanUtil.copyProperties(newUser, SysUser.class));
        final SysUserVO sysUserVO = BeanUtil.copyProperties(this.getById(newUser.getId()), SysUserVO.class);
        userCache.PUT(newUser.getId(), sysUserVO);
        return sysUserVO;
    }

    /**
     * 查询用户分页
     *
     * @param pageNo    当前页
     * @param pageSize  页面大小
     * @param sysUserVO 系统用户值对象
     * @return {@link IPage}<{@link SysUserVO}>
     */
    @Override
    public IPage<SysUserVO> queryUsersByPagination(Integer pageNo, Integer pageSize, SysUserVO sysUserVO) {
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper
                .like(CharSequenceUtil.isNotEmpty(sysUserVO.getAccountName()), SysUser::getAccountName, sysUserVO.getAccountName())
                .like(CharSequenceUtil.isNotEmpty(sysUserVO.getRealName()), SysUser::getRealName, sysUserVO.getRealName())
                .like(CharSequenceUtil.isNotEmpty(sysUserVO.getPhoneNumber()), SysUser::getPhoneNumber, sysUserVO.getPhoneNumber())
                .like(CharSequenceUtil.isNotEmpty(sysUserVO.getEmail()), SysUser::getEmail, sysUserVO.getEmail())
                .orderByDesc(SysUser::getUpdateTime);
        Page<SysUser> page = this.page(new Page<>(pageNo, pageSize), sysUserLambdaQueryWrapper);
        final List<SysUser> sysUserList = page.getRecords();
        IPage<SysUserVO> sysUserVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        final List<SysUserVO> sysUserVOList = Optional.ofNullable(sysUserList).orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysUser -> BeanUtil.copyProperties(sysUser, SysUserVO.class))
                .collect(Collectors.toList());
        sysUserVOPage.setRecords(sysUserVOList);
        return sysUserVOPage;
    }
}
