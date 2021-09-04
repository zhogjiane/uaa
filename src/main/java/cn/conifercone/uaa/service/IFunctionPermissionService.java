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

import cn.conifercone.uaa.domain.entity.SysFunctionPermission;
import cn.conifercone.uaa.domain.vo.SysFunctionPermissionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 功能权限service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
public interface IFunctionPermissionService extends IService<SysFunctionPermission> {

    /**
     * 新建功能权限
     *
     * @param newSysFunctionPermissionVO 新系统功能权限值对象
     * @return {@link SysFunctionPermissionVO}
     */
    SysFunctionPermissionVO newFunctionPermission(SysFunctionPermissionVO newSysFunctionPermissionVO);

    /**
     * 删除功能权限
     *
     * @param functionPermissionIdList 功能权限id列表
     * @return {@link List}<{@link SysFunctionPermissionVO}>
     */
    List<SysFunctionPermissionVO> deleteFunctionPermissions(List<Long> functionPermissionIdList);

    /**
     * 修改功能权限
     *
     * @param sysFunctionPermissionVO 系统功能权限值对象
     * @return {@link SysFunctionPermissionVO}
     */
    SysFunctionPermissionVO modifyFunctionPermission(SysFunctionPermissionVO sysFunctionPermissionVO);

    /**
     * 分页查询功能权限
     *
     * @param pageNo                  当前页
     * @param pageSize                页面大小
     * @param sysFunctionPermissionVO 系统功能权限值对象
     * @return {@link IPage}<{@link SysFunctionPermissionVO}>
     */
    IPage<SysFunctionPermissionVO> pagingQueryFunctionPermissions(Integer pageNo, Integer pageSize, SysFunctionPermissionVO sysFunctionPermissionVO);
}
