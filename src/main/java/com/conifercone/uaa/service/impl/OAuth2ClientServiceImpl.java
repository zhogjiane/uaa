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
import com.conifercone.uaa.domain.entity.SysOAuth2Client;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.SysOAuth2ClientVO;
import com.conifercone.uaa.mapper.OAuth2ClientMapper;
import com.conifercone.uaa.service.IOAuth2ClientService;
import com.conifercone.uaa.util.DataValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OAuth2客户端service实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
@Service
public class OAuth2ClientServiceImpl extends ServiceImpl<OAuth2ClientMapper, SysOAuth2Client> implements IOAuth2ClientService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "OAuth2Client:")
    @SuppressWarnings("unused")
    private Cache<Long, SysOAuth2ClientVO> oauth2ClientCache;

    /**
     * 新建oauth2客户端
     *
     * @param sysOAuth2ClientVO oauth2客户端值对象
     * @return {@link SysOAuth2ClientVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOAuth2ClientVO newOAuth2Client(SysOAuth2ClientVO sysOAuth2ClientVO) {
        if (Boolean.TRUE.equals(DataValidationUtil.determineTheFieldValueDatabaseDuplication(this,
                SysOAuth2Client::getClientId, sysOAuth2ClientVO.getClientId()))) {
            throw new BizException(ResultCode.DUPLICATE_CLIENT_ID, sysOAuth2ClientVO.getClientId());
        }
        final long id = snowflake.nextId();
        sysOAuth2ClientVO.setId(id);
        this.save(BeanUtil.copyProperties(sysOAuth2ClientVO, SysOAuth2Client.class));
        final SysOAuth2ClientVO newSysOAuth2ClientVO = BeanUtil.copyProperties(this.getById(id), SysOAuth2ClientVO.class);
        oauth2ClientCache.PUT(id, newSysOAuth2ClientVO);
        return newSysOAuth2ClientVO;
    }

    /**
     * 删除oauth2客户端
     *
     * @param oauth2ClientIdList oauth2客户端id列表
     * @return {@link List}<{@link SysOAuth2ClientVO}>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysOAuth2ClientVO> deleteOAuth2Client(List<Long> oauth2ClientIdList) {
        final List<SysOAuth2Client> sysOAuth2Clients = this.listByIds(oauth2ClientIdList);
        oauth2ClientCache.REMOVE_ALL(new HashSet<>(oauth2ClientIdList));
        this.removeByIds(oauth2ClientIdList);
        return Optional.ofNullable(sysOAuth2Clients).orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysOAuth2Client -> BeanUtil.copyProperties(sysOAuth2Client, SysOAuth2ClientVO.class))
                .collect(Collectors.toList());
    }

    /**
     * 修改oauth2客户端
     *
     * @param sysOAuth2ClientVO oauth2客户端值对象
     * @return {@link SysOAuth2ClientVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysOAuth2ClientVO modifyOAuth2Client(SysOAuth2ClientVO sysOAuth2ClientVO) {
        final Long id = sysOAuth2ClientVO.getId();
        this.updateById(BeanUtil.copyProperties(sysOAuth2ClientVO, SysOAuth2Client.class));
        final SysOAuth2ClientVO oauth2ClientVO = BeanUtil.copyProperties(this.getById(id), SysOAuth2ClientVO.class);
        oauth2ClientCache.PUT(id, oauth2ClientVO);
        return oauth2ClientVO;
    }

    /**
     * 分页查询oauth2客户端
     *
     * @param pageNo            当前页
     * @param pageSize          页面大小
     * @param sysOAuth2ClientVO oauth2客户端值对象
     * @return {@link IPage}<{@link SysOAuth2ClientVO}>
     */
    @Override
    public IPage<SysOAuth2ClientVO> pagingQueryOAuth2Client(Integer pageNo, Integer pageSize, SysOAuth2ClientVO sysOAuth2ClientVO) {
        LambdaQueryWrapper<SysOAuth2Client> sysOAuth2ClientLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysOAuth2ClientLambdaQueryWrapper
                .like(CharSequenceUtil.isNotBlank(sysOAuth2ClientVO.getClientId()), SysOAuth2Client::getClientId, sysOAuth2ClientVO.getClientId())
                .like(CharSequenceUtil.isNotBlank(sysOAuth2ClientVO.getAllowUrl()), SysOAuth2Client::getAllowUrl, sysOAuth2ClientVO.getAllowUrl())
                .like(CharSequenceUtil.isNotBlank(sysOAuth2ClientVO.getContractScope()), SysOAuth2Client::getContractScope, sysOAuth2ClientVO.getContractScope())
                .orderByDesc(SysOAuth2Client::getUpdateTime);
        Page<SysOAuth2Client> page = this.page(new Page<>(pageNo, pageSize), sysOAuth2ClientLambdaQueryWrapper);
        IPage<SysOAuth2ClientVO> sysOAuth2ClientVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        final List<SysOAuth2ClientVO> sysOAuth2ClientVOList = Optional.ofNullable(page.getRecords()).orElseGet(CollUtil::newLinkedList)
                .stream()
                .map(sysOAuth2Client -> BeanUtil.copyProperties(sysOAuth2Client, SysOAuth2ClientVO.class))
                .collect(Collectors.toList());
        sysOAuth2ClientVOPage.setRecords(sysOAuth2ClientVOList);
        return sysOAuth2ClientVOPage;
    }
}
