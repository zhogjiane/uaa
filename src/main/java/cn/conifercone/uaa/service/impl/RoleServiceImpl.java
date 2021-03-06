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

package cn.conifercone.uaa.service.impl;

import cn.conifercone.uaa.domain.entity.SysRole;
import cn.conifercone.uaa.domain.enumerate.ResultCode;
import cn.conifercone.uaa.domain.exception.BizException;
import cn.conifercone.uaa.domain.vo.SysRoleFunctionPermissionVO;
import cn.conifercone.uaa.domain.vo.SysRoleVO;
import cn.conifercone.uaa.mapper.RoleMapper;
import cn.conifercone.uaa.service.IRoleFunctionPermissionService;
import cn.conifercone.uaa.service.IRoleService;
import cn.conifercone.uaa.util.DataValidationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ??????service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/24
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, SysRole> implements IRoleService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "Role:")
    @SuppressWarnings("unused")
    private Cache<Long, SysRoleVO> rolesCache;

    @Resource
    IRoleFunctionPermissionService roleFunctionPermissionService;

    /**
     * ????????????
     *
     * @param sysRoleVO ?????????????????????
     * @return {@link SysRoleVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRoleVO newRole(SysRoleVO sysRoleVO) {
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysRole::getRoleCode, sysRoleVO.getRoleCode()))) {
            throw new BizException(ResultCode.DUPLICATE_ROLE_CODE, sysRoleVO.getRoleCode());
        }
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysRole::getRoleName, sysRoleVO.getRoleName()))) {
            throw new BizException(ResultCode.DUPLICATE_ROLE_NAME, sysRoleVO.getRoleName());
        }
        final long id = snowflake.nextId();
        sysRoleVO.setId(id);
        this.save(BeanUtil.copyProperties(sysRoleVO, SysRole.class));
        roleFunctionPermissionService.specifyTheRoleToSetFunctionPermissions(sysRoleVO.getId(), sysRoleVO.getFunctionPermissionIdList());
        final SysRole sysRole = this.getById(id);
        final SysRoleVO newSysRoleVO = BeanUtil.copyProperties(sysRole, SysRoleVO.class);
        rolesCache.PUT(id, newSysRoleVO);
        return newSysRoleVO;
    }

    /**
     * ????????????
     *
     * @param roleIds ??????id??????
     * @return {@link List}<{@link SysRoleVO}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysRoleVO> deleteRoles(List<Long> roleIds) {
        final List<SysRole> sysRoleList = this.listByIds(roleIds);
        this.removeByIds(roleIds);
        roleFunctionPermissionService.deleteAllFunctionalPermissionsOfTheRole(roleIds);
        rolesCache.REMOVE_ALL(new HashSet<>(roleIds));
        return Optional.ofNullable(sysRoleList)
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRole -> BeanUtil.copyProperties(sysRole, SysRoleVO.class))
                .collect(Collectors.toList());
    }

    /**
     * ????????????
     *
     * @param sysRoleVO ?????????????????????
     * @return {@link SysRoleVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysRoleVO modifyRole(SysRoleVO sysRoleVO) {
        this.updateById(BeanUtil.copyProperties(sysRoleVO, SysRole.class));
        //????????????????????????
        LinkedList<Long> roleIdList = CollUtil.newLinkedList();
        roleIdList.add(sysRoleVO.getId());
        roleFunctionPermissionService.deleteAllFunctionalPermissionsOfTheRole(roleIdList);
        //??????????????????????????????
        roleFunctionPermissionService.specifyTheRoleToSetFunctionPermissions(sysRoleVO.getId(), sysRoleVO.getFunctionPermissionIdList());
        final Long id = sysRoleVO.getId();
        final SysRole sysRole = this.getById(id);
        final SysRoleVO newSysRoleVO = BeanUtil.copyProperties(sysRole, SysRoleVO.class);
        rolesCache.PUT(id, newSysRoleVO);
        return newSysRoleVO;
    }

    /**
     * ??????????????????
     *
     * @param pageNo    ?????????
     * @param pageSize  ????????????
     * @param sysRoleVO ?????????????????????
     * @return {@link IPage}<{@link SysRoleVO}>
     */
    @Override
    public IPage<SysRoleVO> pagingQueryRole(Integer pageNo, Integer pageSize, SysRoleVO sysRoleVO) {
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper
                .like(CharSequenceUtil.isNotBlank(sysRoleVO.getRoleName()), SysRole::getRoleName, sysRoleVO.getRoleName())
                .like(CharSequenceUtil.isNotBlank(sysRoleVO.getRoleCode()), SysRole::getRoleCode, sysRoleVO.getRoleCode())
                .orderByDesc(SysRole::getUpdateTime);
        Page<SysRole> sysRolePage = this.page(new Page<>(pageNo, pageSize), sysRoleLambdaQueryWrapper);
        IPage<SysRoleVO> sysRoleVOPage = new Page<>(sysRolePage.getCurrent(), sysRolePage.getSize(), sysRolePage.getTotal());
        final List<SysRole> sysRolePageRecords = sysRolePage.getRecords();
        final List<Long> roleIdList = sysRolePageRecords.stream().map(SysRole::getId).collect(Collectors.toList());
        final Map<Long, List<SysRoleFunctionPermissionVO>> roleListQueryRoleFunctionPermissions =
                roleFunctionPermissionService.roleListQueryRoleFunctionPermissions(roleIdList);
        final List<SysRoleVO> sysRoleVOList = Optional.of(sysRolePageRecords)
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRole -> BeanUtil.copyProperties(sysRole, SysRoleVO.class))
                .map(newSysRoleVO ->
                        newSysRoleVO.setFunctionPermissionIdList(
                                roleListQueryRoleFunctionPermissions
                                        .get(newSysRoleVO.getId())
                                        .stream()
                                        .map(SysRoleFunctionPermissionVO::getPermissionId)
                                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
        sysRoleVOPage.setRecords(sysRoleVOList);
        return sysRoleVOPage;
    }
}
