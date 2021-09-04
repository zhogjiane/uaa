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

import cn.conifercone.uaa.domain.entity.SysRole;
import cn.conifercone.uaa.domain.vo.SysRoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/24
 */
public interface IRoleService extends IService<SysRole> {

    /**
     * 新角色
     *
     * @param sysRoleVO 系统角色值对象
     * @return {@link SysRoleVO}
     */
    SysRoleVO newRole(SysRoleVO sysRoleVO);

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return {@link List}<{@link SysRoleVO}>
     */
    List<SysRoleVO> deleteRoles(List<Long> roleIds);

    /**
     * 修改角色
     *
     * @param sysRoleVO 系统角色值对象
     * @return {@link SysRoleVO}
     */
    SysRoleVO modifyRole(SysRoleVO sysRoleVO);

    /**
     * 分页查询角色
     *
     * @param pageNo    当前页
     * @param pageSize  页面大小
     * @param sysRoleVO 系统角色值对象
     * @return {@link IPage}<{@link SysRoleVO}>
     */
    IPage<SysRoleVO> pagingQueryRole(Integer pageNo, Integer pageSize, SysRoleVO sysRoleVO);

}
