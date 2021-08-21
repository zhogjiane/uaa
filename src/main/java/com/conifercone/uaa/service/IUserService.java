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

package com.conifercone.uaa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.conifercone.uaa.domain.entity.SysUser;
import com.conifercone.uaa.domain.vo.SysUserVO;

import java.util.List;

/**
 * 用户service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
public interface IUserService extends IService<SysUser> {

    /**
     * 新增用户
     *
     * @param newUser 新用户
     * @return {@link SysUserVO}
     */
    SysUserVO newUsers(SysUserVO newUser);


    /**
     * 删除用户
     *
     * @param sysUserIdList 系统用户id集合
     * @return {@link List}<{@link SysUserVO}>
     */
    List<SysUserVO> deleteUsers(List<Long> sysUserIdList);


    /**
     * 修改用户
     *
     * @param newUser 新用户
     * @return {@link SysUserVO}
     */
    SysUserVO modifyUser(SysUserVO newUser);


    /**
     * 查询用户分页
     *
     * @param pageNo    当前页
     * @param pageSize  页面大小
     * @param sysUserVO 系统用户值对象
     * @return {@link IPage}<{@link SysUserVO}>
     */
    IPage<SysUserVO> queryUsersByPagination(Integer pageNo, Integer pageSize, SysUserVO sysUserVO);
}
