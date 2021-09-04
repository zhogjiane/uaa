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

package cn.conifercone.uaa.service.impl;

import cn.conifercone.uaa.domain.entity.SysUserRole;
import cn.conifercone.uaa.domain.vo.SysUserRoleVO;
import cn.conifercone.uaa.mapper.UserRoleMapper;
import cn.conifercone.uaa.service.IUserRoleService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户角色service实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, SysUserRole> implements IUserRoleService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "UserRole:")
    @SuppressWarnings("unused")
    private Cache<Long, SysUserRoleVO> userRoleCache;

    /**
     * 基于用户id查询用户角色关系
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysUserRole}>
     */
    @Override
    public List<SysUserRoleVO> queryUserRoleRelationshipBasedOnUserId(Long userId) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(userId), SysUserRole::getUserId, userId);
        return Optional.ofNullable(this.list(sysUserRoleLambdaQueryWrapper))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysUserRole -> BeanUtil.copyProperties(sysUserRole, SysUserRoleVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 基于用户id列表查询用户角色关系
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link List}<{@link SysUserRoleVO}>>
     */
    @Override
    public Map<Long, List<SysUserRoleVO>> queryUserRoleRelationshipBasedOnUserId(List<Long> userIdList) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.in(CollUtil.isNotEmpty(userIdList), SysUserRole::getUserId, userIdList);
        return Optional.ofNullable(this.list(sysUserRoleLambdaQueryWrapper))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysUserRole -> BeanUtil.copyProperties(sysUserRole, SysUserRoleVO.class))
                .collect(Collectors.groupingBy(SysUserRoleVO::getUserId));
    }

    /**
     * 指定用户设置角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void specifyUserSettingsRole(Long userId, List<Long> roleIds) {
        if (CollUtil.isNotEmpty(roleIds)) {
            final List<SysUserRole> sysUserRoleList = roleIds.stream().map(roleId -> {
                final long userRoleId = snowflake.nextId();
                final SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setId(userRoleId);
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);
                return sysUserRole;
            }).collect(Collectors.toList());
            this.saveBatch(sysUserRoleList);
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
     * 删除用户所有的角色
     *
     * @param userIdList 用户id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllRolesOfTheUser(List<Long> userIdList) {
        LambdaQueryWrapper<SysUserRole> sysUserRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserRoleLambdaQueryWrapper.in(CollUtil.isNotEmpty(userIdList), SysUserRole::getUserId, userIdList);
        List<SysUserRoleVO> sysUserRoleVOS = Optional
                .ofNullable(this.list(sysUserRoleLambdaQueryWrapper))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysUserRole -> BeanUtil.copyProperties(sysUserRole, SysUserRoleVO.class))
                .collect(Collectors.toList());
        List<Long> roleIdList = sysUserRoleVOS.stream().map(SysUserRoleVO::getId).collect(Collectors.toList());
        this.removeByIds(roleIdList);
        userRoleCache.REMOVE_ALL(new HashSet<>(roleIdList));
    }
}
