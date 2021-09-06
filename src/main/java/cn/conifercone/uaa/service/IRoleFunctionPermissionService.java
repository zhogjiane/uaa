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

package cn.conifercone.uaa.service;

import cn.conifercone.uaa.domain.entity.SysRoleFunctionPermission;
import cn.conifercone.uaa.domain.vo.SysRoleFunctionPermissionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 角色功能权限service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
public interface IRoleFunctionPermissionService extends IService<SysRoleFunctionPermission> {

    /**
     * 汇总角色功能权限
     *
     * @param roleIdList 角色id集合
     * @return {@link List}<{@link Long}>
     */
    List<Long> summaryRoleFunctionPermissions(List<Long> roleIdList);

    /**
     * 角色列表查询角色功能权限
     *
     * @param roleIdList 角色id列表
     * @return {@link Map}<{@link Long}, {@link List}<{@link SysRoleFunctionPermissionVO}>>
     */
    Map<Long, List<SysRoleFunctionPermissionVO>> roleListQueryRoleFunctionPermissions(List<Long> roleIdList);

    /**
     * 指定角色权限设置功能
     *
     * @param roleId                   角色id
     * @param functionPermissionIdList 功能权限id列表
     */
    void specifyTheRoleToSetFunctionPermissions(Long roleId, List<Long> functionPermissionIdList);

    /**
     * 删除角色所有的功能权限
     *
     * @param roleIdList 角色id列表
     */
    void deleteAllFunctionalPermissionsOfTheRole(List<Long> roleIdList);
}
