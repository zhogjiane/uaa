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
import com.conifercone.uaa.domain.vo.SysFunctionPermissionVO;
import com.conifercone.uaa.service.IFunctionPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 功能权限
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@RestController
@RequestMapping("/functionPermission")
@Api(value = "功能权限", tags = "功能权限")
public class FunctionPermissionController {

    @Resource
    IFunctionPermissionService functionPermissionService;

    @PostMapping
    @ApiOperation("新建功能权限")
    public SysFunctionPermissionVO newFunctionPermission(@RequestBody @Valid SysFunctionPermissionVO newSysFunctionPermissionVO) {
        return functionPermissionService.newFunctionPermission(newSysFunctionPermissionVO);
    }

    @DeleteMapping
    @ApiOperation("删除功能权限")
    public List<SysFunctionPermissionVO> deleteFunctionPermissions(@RequestBody @Valid List<Long> sysFunctionPermissionIdList) {
        return functionPermissionService.deleteFunctionPermissions(sysFunctionPermissionIdList);
    }

    @PutMapping
    @ApiOperation("修改功能权限")
    public SysFunctionPermissionVO modifyFunctionPermission(@RequestBody @Valid SysFunctionPermissionVO sysFunctionPermissionVO) {
        return functionPermissionService.modifyFunctionPermission(sysFunctionPermissionVO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询功能权限")
    public IPage<SysFunctionPermissionVO> pagingQueryFunctionPermissions(Integer pageNo, Integer pageSize, SysFunctionPermissionVO sysFunctionPermissionVO) {
        return functionPermissionService.pagingQueryFunctionPermissions(pageNo, pageSize, sysFunctionPermissionVO);
    }
    
}
