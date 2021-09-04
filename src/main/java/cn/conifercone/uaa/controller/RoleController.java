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

package cn.conifercone.uaa.controller;

import cn.conifercone.uaa.domain.vo.SysRoleVO;
import cn.conifercone.uaa.service.IRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色controller
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/24
 */
@RestController
@RequestMapping("/role")
@Slf4j
@Api(value = "角色管理", tags = "角色管理")
public class RoleController {

    @Resource
    IRoleService roleService;

    @PostMapping
    @ApiOperation("新增角色")
    public SysRoleVO newRole(@RequestBody @Valid SysRoleVO newRole) {
        return roleService.newRole(newRole);
    }

    @DeleteMapping
    @ApiOperation("删除角色")
    public List<SysRoleVO> deleteRoles(@RequestBody @Valid List<Long> roleIds) {
        return roleService.deleteRoles(roleIds);
    }

    @PutMapping
    @ApiOperation("修改角色")
    public SysRoleVO modifyRole(@RequestBody @Valid SysRoleVO sysRoleVO) {
        return roleService.modifyRole(sysRoleVO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询角色")
    public IPage<SysRoleVO> pagingQueryRole(Integer pageNo, Integer pageSize, SysRoleVO sysRoleVO) {
        return roleService.pagingQueryRole(pageNo, pageSize, sysRoleVO);
    }
}
