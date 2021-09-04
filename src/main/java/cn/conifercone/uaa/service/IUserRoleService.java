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

import cn.conifercone.uaa.domain.entity.SysUserRole;
import cn.conifercone.uaa.domain.vo.SysUserRoleVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * 用户角色service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
public interface IUserRoleService extends IService<SysUserRole> {

    /**
     * 基于用户id查询用户角色关系
     *
     * @param userId 用户id
     * @return {@link List}<{@link SysUserRole}>
     */
    List<SysUserRoleVO> queryUserRoleRelationshipBasedOnUserId(Long userId);

    /**
     * 基于用户id列表查询用户角色关系
     *
     * @param userIdList 用户id列表
     * @return {@link Map}<{@link Long}, {@link List}<{@link SysUserRoleVO}>>
     */
    Map<Long, List<SysUserRoleVO>> queryUserRoleRelationshipBasedOnUserId(List<Long> userIdList);

    /**
     * 指定用户设置角色
     *
     * @param userId  用户id
     * @param roleIds 角色id
     */
    void specifyUserSettingsRole(Long userId, List<Long> roleIds);


    /**
     * 删除用户所有的角色
     *
     * @param userIdList 用户id集合
     */
    void deleteAllRolesOfTheUser(List<Long> userIdList);
}
