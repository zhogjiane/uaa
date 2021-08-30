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

package com.conifercone.uaa.service.impl;

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
import com.conifercone.uaa.domain.entity.SysFunctionPermission;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.SysFunctionPermissionVO;
import com.conifercone.uaa.mapper.FunctionPermissionMapper;
import com.conifercone.uaa.service.IFunctionPermissionService;
import com.conifercone.uaa.util.DataValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 功能权限实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@Service
public class FunctionPermissionServiceImpl extends ServiceImpl<FunctionPermissionMapper, SysFunctionPermission>
        implements IFunctionPermissionService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "FunctionPermission:")
    @SuppressWarnings("unused")
    private Cache<Long, SysFunctionPermissionVO> functionPermissionCache;

    /**
     * 新功能权限
     *
     * @param newSysFunctionPermissionVO 新系统功能权限值对象
     * @return {@link SysFunctionPermissionVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysFunctionPermissionVO newFunctionPermission(SysFunctionPermissionVO newSysFunctionPermissionVO) {
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysFunctionPermission::getPermissionCode, newSysFunctionPermissionVO.getPermissionCode()))) {
            throw new BizException(ResultCode.DUPLICATE_FUNCTION_PERMISSION_CODE, newSysFunctionPermissionVO.getPermissionCode());
        }
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysFunctionPermission::getPermissionName, newSysFunctionPermissionVO.getPermissionName()))) {
            throw new BizException(ResultCode.DUPLICATE_FUNCTION_PERMISSION_NAME, newSysFunctionPermissionVO.getPermissionName());
        }
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysFunctionPermission::getPermissionScope, newSysFunctionPermissionVO.getPermissionScope()))) {
            throw new BizException(ResultCode.DUPLICATE_FUNCTION_PERMISSION_SCOPE, newSysFunctionPermissionVO.getPermissionScope());
        }
        final long id = snowflake.nextId();
        newSysFunctionPermissionVO.setId(id);
        this.save(BeanUtil.copyProperties(newSysFunctionPermissionVO, SysFunctionPermission.class));
        final SysFunctionPermission sysFunctionPermission = this.getById(id);
        final SysFunctionPermissionVO sysFunctionPermissionVO = BeanUtil
                .copyProperties(sysFunctionPermission, SysFunctionPermissionVO.class);
        functionPermissionCache.PUT(id, sysFunctionPermissionVO);
        return sysFunctionPermissionVO;
    }

    /**
     * 删除功能权限
     *
     * @param functionPermissionIdList 功能权限id列表
     * @return {@link List}<{@link SysFunctionPermissionVO}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysFunctionPermissionVO> deleteFunctionPermissions(List<Long> functionPermissionIdList) {
        final List<SysFunctionPermission> sysFunctionPermissions = this.listByIds(functionPermissionIdList);
        this.removeByIds(functionPermissionIdList);
        functionPermissionCache.REMOVE_ALL(new HashSet<>(functionPermissionIdList));
        return Optional.ofNullable(sysFunctionPermissions)
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysFunctionPermission -> BeanUtil.copyProperties(sysFunctionPermission, SysFunctionPermissionVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 修改功能权限
     *
     * @param sysFunctionPermissionVO 系统功能权限值对象
     * @return {@link SysFunctionPermissionVO}
     */
    @Override
    public SysFunctionPermissionVO modifyFunctionPermission(SysFunctionPermissionVO sysFunctionPermissionVO) {
        this.updateById(BeanUtil.copyProperties(sysFunctionPermissionVO, SysFunctionPermission.class));
        final Long id = sysFunctionPermissionVO.getId();
        final SysFunctionPermission sysFunctionPermission = this.getById(id);
        final SysFunctionPermissionVO newSysFunctionPermissionVO = BeanUtil.copyProperties(sysFunctionPermission, SysFunctionPermissionVO.class);
        functionPermissionCache.PUT(id, newSysFunctionPermissionVO);
        return newSysFunctionPermissionVO;
    }

    /**
     * 分页查询功能权限
     *
     * @param pageNo                  当前页
     * @param pageSize                页面大小
     * @param sysFunctionPermissionVO 系统功能权限值对象
     * @return {@link IPage}<{@link SysFunctionPermissionVO}>
     */
    @Override
    public IPage<SysFunctionPermissionVO> pagingQueryFunctionPermissions(Integer pageNo, Integer pageSize, SysFunctionPermissionVO sysFunctionPermissionVO) {
        LambdaQueryWrapper<SysFunctionPermission> sysFunctionPermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysFunctionPermissionLambdaQueryWrapper
                .like(CharSequenceUtil.isNotBlank(
                                sysFunctionPermissionVO.getPermissionCode()),
                        SysFunctionPermission::getPermissionCode,
                        sysFunctionPermissionVO.getPermissionCode())
                .like(CharSequenceUtil.isNotBlank(
                                sysFunctionPermissionVO.getPermissionName()),
                        SysFunctionPermission::getPermissionName,
                        sysFunctionPermissionVO.getPermissionName());
        Page<SysFunctionPermission> page = this.page(new Page<>(pageNo, pageSize), sysFunctionPermissionLambdaQueryWrapper);
        IPage<SysFunctionPermissionVO> sysFunctionPermissionVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        final List<SysFunctionPermission> sysFunctionPermissionList = page.getRecords();
        final List<SysFunctionPermissionVO> sysFunctionPermissionVOList = Optional.ofNullable(sysFunctionPermissionList)
                .orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysFunctionPermission -> BeanUtil.copyProperties(sysFunctionPermission, SysFunctionPermissionVO.class))
                .collect(Collectors.toList());
        sysFunctionPermissionVOPage.setRecords(sysFunctionPermissionVOList);
        return sysFunctionPermissionVOPage;
    }
}
