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

package com.conifercone.uaa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.conifercone.uaa.domain.vo.SysUserVO;
import com.conifercone.uaa.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 用户逻辑控制
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "用户管理", tags = "用户管理")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping
    @ApiOperation("新增用户")
    public SysUserVO newUsers(@RequestBody @Valid SysUserVO newUser) {
        return userService.newUsers(newUser);
    }

    @DeleteMapping
    @ApiOperation("删除用户")
    public Boolean deleteUsers(@RequestBody List<Long> sysUserIdList) {
        return userService.deleteUsers(sysUserIdList);
    }

    @PostMapping
    @ApiOperation("修改用户")
    public SysUserVO modifyUser(@RequestBody SysUserVO newUser) {
        return userService.modifyUser(newUser);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询用户")
    public IPage<SysUserVO> queryUsersByPagination(Integer pageNo, Integer pageSize, SysUserVO sysUserVO) {
        return userService.queryUsersByPagination(pageNo, pageSize, sysUserVO);
    }
}
