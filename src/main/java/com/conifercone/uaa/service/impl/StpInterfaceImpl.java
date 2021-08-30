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

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.conifercone.uaa.domain.entity.SysFunctionPermission;
import com.conifercone.uaa.domain.entity.SysRole;
import com.conifercone.uaa.domain.vo.SysRoleFunctionPermissionVO;
import com.conifercone.uaa.domain.vo.SysUserRoleVO;
import com.conifercone.uaa.service.IFunctionPermissionService;
import com.conifercone.uaa.service.IRoleFunctionPermissionService;
import com.conifercone.uaa.service.IRoleService;
import com.conifercone.uaa.service.IUserRoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 自定义权限验证接口扩展
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    IUserRoleService userRoleService;

    @Resource
    IRoleFunctionPermissionService roleFunctionPermissionService;

    @Resource
    IFunctionPermissionService functionPermissionService;

    @Resource
    IRoleService roleService;

    /**
     * 获得权限列表
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //查询当前用户所拥有的角色
        List<Long> roleIdList = Optional.ofNullable(userRoleService.queryUserRoleRelationshipBasedOnUserId(Long.parseLong(String.valueOf(loginId))))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysUserRoleVO::getRoleId)
                .collect(Collectors.toList());
        //查询角色对应的权限
        List<Long> permissionIdList = roleFunctionPermissionService.summaryRoleFunctionPermissions(roleIdList)
                .stream()
                .map(SysRoleFunctionPermissionVO::getPermissionId)
                .collect(Collectors.toList());
        return Optional.ofNullable(functionPermissionService.listByIds(permissionIdList))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysFunctionPermission::getPermissionCode)
                .collect(Collectors.toList());
    }

    /**
     * 获取角色列表
     *
     * @param loginId   登录id
     * @param loginType 登录类型
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<Long> roleIdList = userRoleService
                .queryUserRoleRelationshipBasedOnUserId(Long.parseLong(String.valueOf(loginId)))
                .stream()
                .map(SysUserRoleVO::getRoleId)
                .collect(Collectors.toList());
        return Optional.ofNullable(roleService.listByIds(roleIdList))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
    }
}
