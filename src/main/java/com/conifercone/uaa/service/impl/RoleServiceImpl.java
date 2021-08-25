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
import com.conifercone.uaa.domain.entity.SysRole;
import com.conifercone.uaa.domain.vo.SysRoleVO;
import com.conifercone.uaa.mapper.RoleMapper;
import com.conifercone.uaa.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements IRoleService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "Role:")
    private Cache<Long, SysRoleVO> rolesCache;

    /**
     * 新增角色
     *
     * @param sysRoleVO 系统角色值对象
     * @return {@link SysRoleVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRoleVO newRole(SysRoleVO sysRoleVO) {
        long id = snowflake.nextId();
        sysRoleVO.setId(id);
        this.save(BeanUtil.copyProperties(sysRoleVO, SysRole.class));
        SysRole sysRole = this.getById(id);
        SysRoleVO newSysRoleVO = BeanUtil.copyProperties(sysRole, SysRoleVO.class);
        rolesCache.PUT(id, newSysRoleVO);
        return newSysRoleVO;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return {@link List}<{@link SysRoleVO}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysRoleVO> deleteRoles(List<Long> roleIds) {
        this.removeByIds(roleIds);
        rolesCache.REMOVE_ALL(new HashSet<>(roleIds));
        return Optional.ofNullable(this.listByIds(roleIds))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRole -> BeanUtil.copyProperties(sysRole, SysRoleVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 修改角色
     *
     * @param sysRoleVO 系统角色值对象
     * @return {@link SysRoleVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRoleVO modifyRole(SysRoleVO sysRoleVO) {
        this.updateById(BeanUtil.copyProperties(sysRoleVO, SysRole.class));
        Long id = sysRoleVO.getId();
        SysRole sysRole = this.getById(id);
        SysRoleVO newSysRoleVO = BeanUtil.copyProperties(sysRole, SysRoleVO.class);
        rolesCache.PUT(id, newSysRoleVO);
        return newSysRoleVO;
    }

    /**
     * 分页查询角色
     *
     * @param pageNo    当前页
     * @param pageSize  页面大小
     * @param sysRoleVO 系统角色值对象
     * @return {@link IPage}<{@link SysRoleVO}>
     */
    @Override
    public IPage<SysRoleVO> pagingQueryRole(Integer pageNo, Integer pageSize, SysRoleVO sysRoleVO) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper
                .like(CharSequenceUtil.isNotBlank(sysRoleVO.getRoleName()), SysRole::getRoleName, sysRoleVO.getRoleName())
                .like(CharSequenceUtil.isNotBlank(sysRoleVO.getRoleCode()), SysRole::getRoleCode, sysRoleVO.getRoleCode())
                .orderByDesc(SysRole::getUpdateTime);
        Page<SysRole> sysRolePage = this.page(new Page<>(pageNo, pageSize), sysRoleLambdaQueryWrapper);
        IPage<SysRoleVO> sysRoleVOPage = new Page<>(sysRolePage.getCurrent(), sysRolePage.getSize(), sysRolePage.getTotal());
        List<SysRoleVO> sysRoleVOList = Optional.ofNullable(sysRolePage.getRecords())
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRole -> BeanUtil.copyProperties(sysRole, SysRoleVO.class))
                .collect(Collectors.toList());
        sysRoleVOPage.setRecords(sysRoleVOList);
        return sysRoleVOPage;
    }
}