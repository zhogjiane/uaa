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

import cn.conifercone.uaa.domain.entity.SysRoleFunctionPermission;
import cn.conifercone.uaa.domain.vo.SysRoleFunctionPermissionVO;
import cn.conifercone.uaa.mapper.RoleFunctionPermissionMapper;
import cn.conifercone.uaa.service.IRoleFunctionPermissionService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Snowflake;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色功能权限实现类
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@Service
public class RoleFunctionPermissionServiceImpl extends
        ServiceImpl<RoleFunctionPermissionMapper, SysRoleFunctionPermission> implements IRoleFunctionPermissionService {


    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "RoleFunctionPermission:")
    @SuppressWarnings("unused")
    private Cache<Long, SysRoleFunctionPermissionVO> roleFunctionPermissionCache;

    /**
     * 汇总角色功能权限
     *
     * @param roleIdList 角色id集合
     * @return {@link List}<{@link SysRoleFunctionPermissionVO}>
     */
    @Override
    public List<SysRoleFunctionPermissionVO> summaryRoleFunctionPermissions(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleFunctionPermission> sysRoleFunctionPermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleFunctionPermissionLambdaQueryWrapper
                .in(CollUtil.isNotEmpty(roleIdList), SysRoleFunctionPermission::getRoleId, roleIdList);
        List<Long> permissionIdList = Optional.ofNullable(this.list(sysRoleFunctionPermissionLambdaQueryWrapper))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(SysRoleFunctionPermission::getPermissionId)
                .distinct()
                .collect(Collectors.toList());
        return Optional.ofNullable(this.listByIds(permissionIdList)).orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRoleFunctionPermission -> BeanUtil.copyProperties(sysRoleFunctionPermission, SysRoleFunctionPermissionVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 角色列表查询角色功能权限
     *
     * @param roleIdList 角色id列表
     * @return {@link Map}<{@link Long}, {@link List}<{@link SysRoleFunctionPermissionVO}>>
     */
    @Override
    public Map<Long, List<SysRoleFunctionPermissionVO>> roleListQueryRoleFunctionPermissions(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleFunctionPermission> sysRoleFunctionPermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleFunctionPermissionLambdaQueryWrapper.in(CollUtil.isNotEmpty(roleIdList), SysRoleFunctionPermission::getRoleId, roleIdList);
        return Optional.ofNullable(this.list(sysRoleFunctionPermissionLambdaQueryWrapper))
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysRoleFunctionPermission -> BeanUtil.copyProperties(sysRoleFunctionPermission, SysRoleFunctionPermissionVO.class))
                .collect(Collectors.groupingBy(SysRoleFunctionPermissionVO::getRoleId));
    }

    /**
     * 指定角色权限设置功能权限
     *
     * @param roleId                   角色id
     * @param functionPermissionIdList 功能权限id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void specifyTheRoleToSetFunctionPermissions(Long roleId, List<Long> functionPermissionIdList) {
        List<SysRoleFunctionPermission> sysRoleFunctionPermissions = functionPermissionIdList.stream().map(functionPermissionId -> {
            final long id = snowflake.nextId();
            final SysRoleFunctionPermission newSysRoleFunctionPermission = new SysRoleFunctionPermission();
            newSysRoleFunctionPermission.setId(id);
            newSysRoleFunctionPermission.setPermissionId(functionPermissionId);
            newSysRoleFunctionPermission.setRoleId(roleId);
            return newSysRoleFunctionPermission;
        }).collect(Collectors.toList());
        this.saveBatch(sysRoleFunctionPermissions);
        Map<Long, SysRoleFunctionPermissionVO> sysRoleFunctionPermissionVOMap = sysRoleFunctionPermissions
                .stream()
                .collect(Collectors.toMap(SysRoleFunctionPermission::getId,
                        sysRoleFunctionPermission ->
                                BeanUtil.copyProperties(sysRoleFunctionPermission, SysRoleFunctionPermissionVO.class)));
        roleFunctionPermissionCache.PUT_ALL(sysRoleFunctionPermissionVOMap);
    }

    /**
     * 删除角色所有的功能权限
     *
     * @param roleIdList 角色id列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllFunctionalPermissionsOfTheRole(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleFunctionPermission> sysRoleFunctionPermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleFunctionPermissionLambdaQueryWrapper.in(CollUtil.isNotEmpty(roleIdList), SysRoleFunctionPermission::getRoleId, roleIdList);
        this.remove(sysRoleFunctionPermissionLambdaQueryWrapper);
        roleFunctionPermissionCache.REMOVE_ALL(new HashSet<>(roleIdList));
    }
}
