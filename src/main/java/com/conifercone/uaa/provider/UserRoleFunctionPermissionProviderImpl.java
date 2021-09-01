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

package com.conifercone.uaa.provider;

import cn.conifercone.dubbo.provider.UserRoleFunctionPermissionProvider;
import cn.hutool.core.collection.CollUtil;
import com.conifercone.uaa.domain.entity.SysFunctionPermission;
import com.conifercone.uaa.domain.entity.SysRole;
import com.conifercone.uaa.domain.vo.SysRoleFunctionPermissionVO;
import com.conifercone.uaa.domain.vo.SysUserRoleVO;
import com.conifercone.uaa.service.IFunctionPermissionService;
import com.conifercone.uaa.service.IRoleFunctionPermissionService;
import com.conifercone.uaa.service.IRoleService;
import com.conifercone.uaa.service.IUserRoleService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户角色功能权限提供方实现类
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@DubboService
@SuppressWarnings("unused")
public class UserRoleFunctionPermissionProviderImpl implements UserRoleFunctionPermissionProvider {

    @Resource
    IUserRoleService userRoleService;

    @Resource
    IRoleFunctionPermissionService roleFunctionPermissionService;

    @Resource
    IFunctionPermissionService functionPermissionService;

    @Resource
    IRoleService roleService;

    /**
     * 获取用户功能权限
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> obtainUserFunctionPermissions(Long userId) {
        //查询当前用户所拥有的角色
        List<Long> roleIdList = Optional.ofNullable(userRoleService.queryUserRoleRelationshipBasedOnUserId(Long.parseLong(String.valueOf(userId))))
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
     * 得到所有用户的角色
     *
     * @param userId 用户id
     * @return {@link List}<{@link String}>
     */
    @Override
    public List<String> getAllRoleCodesOfTheUser(Long userId) {
        List<Long> roleIdList = userRoleService.queryUserRoleRelationshipBasedOnUserId(userId)
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
