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

import com.baomidou.mybatisplus.extension.service.IService;
import com.conifercone.uaa.domain.entity.SysOAuth2Client;
import com.conifercone.uaa.domain.vo.SysOAuth2ClientVO;

import java.util.List;

/**
 * OAuth2客户端service
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
public interface IOAuth2ClientService extends IService<SysOAuth2Client> {

    /**
     * 新建oauth2客户端
     *
     * @param sysOAuth2ClientVO oauth2客户端值对象
     * @return {@link SysOAuth2ClientVO}
     */
    SysOAuth2ClientVO newOAuth2Client(SysOAuth2ClientVO sysOAuth2ClientVO);


    /**
     * 删除oauth2客户端
     *
     * @param oauth2ClientIdList oauth2客户端id列表
     * @return {@link List}<{@link SysOAuth2ClientVO}>
     */
    List<SysOAuth2ClientVO> deleteOAuth2Client(List<Long> oauth2ClientIdList);


    /**
     * 修改oauth2客户端
     *
     * @param sysOAuth2ClientVO oauth2客户端值对象
     * @return {@link SysOAuth2ClientVO}
     */
    SysOAuth2ClientVO modifyOAuth2Client(SysOAuth2ClientVO sysOAuth2ClientVO);


}
