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

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.hutool.core.util.ObjectUtil;
import com.conifercone.uaa.domain.entity.SysOAuth2Client;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.mapper.OAuth2ClientMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Sa-Token OAuth2.0 整合实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/17
 */
@Component
public class SaOAuth2TemplateImpl extends SaOAuth2Template {

    @Resource
    OAuth2ClientMapper oauth2ClientMapper;

    /**
     * 根据 id 获取 Client 信息
     *
     * @param clientId 客户端id
     * @return {@link SaClientModel}
     */
    @Override
    public SaClientModel getClientModel(String clientId) {
        SysOAuth2Client sysOauth2Client = oauth2ClientMapper.queryClientInfoByClientId(clientId);
        if (ObjectUtil.isNotNull(sysOauth2Client)) {
            return new SaClientModel()
                    .setClientId(sysOauth2Client.getClientId())
                    .setClientSecret(sysOauth2Client.getClientSecret())
                    .setAllowUrl(sysOauth2Client.getAllowUrl())
                    .setContractScope(sysOauth2Client.getContractScope());
        }
        throw new BizException(ResultCode.CLIENT_NOT_EXIST);
    }

    /**
     * 根据ClientId 和 LoginId 获取openid
     *
     * @param clientId 客户端id
     * @param loginId  登录id
     * @return {@link String}
     */
    @Override
    public String getOpenid(String clientId, Object loginId) {
        return "gr_SwoIN0MC1ewxHX_vfCW3BothWDZMMtx__";
    }
}
