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
import cn.hutool.core.lang.Snowflake;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conifercone.uaa.domain.entity.SysOAuth2Client;
import com.conifercone.uaa.domain.vo.SysOAuth2ClientVO;
import com.conifercone.uaa.mapper.OAuth2Mapper;
import com.conifercone.uaa.service.IOAuth2ClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * OAuth2客户端service实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
@Service
public class OAuth2ClientServiceImpl extends ServiceImpl<OAuth2Mapper, SysOAuth2Client> implements IOAuth2ClientService {

    @Resource
    Snowflake snowflake;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "OAuth2Client:")
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
        long id = snowflake.nextId();
        sysOAuth2ClientVO.setId(id);
        this.save(BeanUtil.copyProperties(sysOAuth2ClientVO, SysOAuth2Client.class));
        SysOAuth2ClientVO newSysOAuth2ClientVO = BeanUtil.copyProperties(this.getById(id), SysOAuth2ClientVO.class);
        oauth2ClientCache.PUT(id, newSysOAuth2ClientVO);
        return newSysOAuth2ClientVO;
    }
}
