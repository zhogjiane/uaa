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

package cn.conifercone.uaa.provider;

import cn.conifercone.grpc.provider.RoleCodes;
import cn.conifercone.grpc.provider.UserFunctionPermissions;
import cn.conifercone.grpc.provider.UserId;
import cn.conifercone.grpc.provider.UserRoleFunctionPermissionProviderGrpc;
import cn.conifercone.uaa.domain.entity.SysRole;
import cn.conifercone.uaa.domain.vo.SysUserRoleVO;
import cn.conifercone.uaa.service.IFunctionPermissionService;
import cn.conifercone.uaa.service.IRoleFunctionPermissionService;
import cn.conifercone.uaa.service.IRoleService;
import cn.conifercone.uaa.service.IUserRoleService;
import cn.hutool.core.collection.CollUtil;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

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
@GrpcService
@SuppressWarnings("unused")
public class UserRoleFunctionPermissionProviderImpl extends UserRoleFunctionPermissionProviderGrpc.UserRoleFunctionPermissionProviderImplBase {

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
     * @param request          请求
     * @param responseObserver 响应的观察者
     */
    @Override
    public void obtainUserFunctionPermissions(UserId request, StreamObserver<UserFunctionPermissions> responseObserver) {
        Long userId = Long.parseLong(request.getUserId());
        //查询当前用户所拥有的角色
        List<Long> roleIdList = Optional.ofNullable(userRoleService.queryUserRoleRelationshipBasedOnUserId(Long.parseLong(String.valueOf(userId))))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysUserRoleVO::getRoleId)
                .collect(Collectors.toList());
        //查询角色对应的权限
        List<Long> permissionIdList = roleFunctionPermissionService.summaryRoleFunctionPermissions(roleIdList);
        List<String> collect = Optional.ofNullable(functionPermissionService.listByIds(permissionIdList))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysFunctionPermission -> sysFunctionPermission.getPermissionScope().concat(":").concat(sysFunctionPermission.getPermissionCode()))
                .collect(Collectors.toList());
        collect.forEach(res -> responseObserver.onNext(UserFunctionPermissions.newBuilder().setPermissions(res).build()));
        responseObserver.onCompleted();
    }

    /**
     * 得到所有用户的角色
     *
     * @param request          请求
     * @param responseObserver 响应的观察者
     */
    @Override
    public void getAllRoleCodesOfTheUser(UserId request, StreamObserver<RoleCodes> responseObserver) {
        Long userId = Long.parseLong(request.getUserId());
        List<Long> roleIdList = userRoleService.queryUserRoleRelationshipBasedOnUserId(userId)
                .stream()
                .map(SysUserRoleVO::getRoleId)
                .collect(Collectors.toList());
        List<String> collect = Optional.ofNullable(roleService.listByIds(roleIdList))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
        collect.forEach(res -> responseObserver.onNext(RoleCodes.newBuilder().setRoleCode(res).build()));
        responseObserver.onCompleted();
    }
}
