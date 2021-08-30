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

import cn.hutool.core.collection.CollUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.conifercone.uaa.UaaApplicationServer;
import com.conifercone.uaa.domain.vo.SysOAuth2ClientVO;
import com.conifercone.uaa.service.IOAuth2ClientService;
import com.conifercone.uaa.util.MockLogin;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * OAuth2客户端单元测试
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/26
 */
@SpringBootTest(classes = UaaApplicationServer.class)
class OAuth2ClientServiceImplTest {

    @Resource
    IOAuth2ClientService oauth2ClientService;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "OAuth2Client:")
    @SuppressWarnings("unused")
    private Cache<Long, SysOAuth2ClientVO> oauth2ClientCache;

    private static final Logger logger = Logger.getLogger(FunctionPermissionServiceImplTest.class);

    @BeforeEach
    void loginBeforeTesting() {
        MockLogin.loginIn();
    }

    @AfterEach
    void logOutAfterTesting() {
        MockLogin.loginOut();
    }

    @Test
    @Transactional
    @Rollback
    void newOAuth2Client() {
        SysOAuth2ClientVO sysOAuth2ClientVO = new SysOAuth2ClientVO();
        sysOAuth2ClientVO.setClientId("测试客户端").setClientSecret("测试客户端").setAllowUrl("*").setContractScope("all");
        SysOAuth2ClientVO newOAuth2Client = oauth2ClientService.newOAuth2Client(sysOAuth2ClientVO);
        logger.info(">>>>>>>>>>>>>>>>" + newOAuth2Client.toString());
        Assertions.assertNotNull(newOAuth2Client);
        oauth2ClientCache.REMOVE(newOAuth2Client.getId());
    }

    @Test
    @Transactional
    @Rollback
    void deleteOAuth2Client() {
        SysOAuth2ClientVO sysOAuth2ClientVO = new SysOAuth2ClientVO();
        sysOAuth2ClientVO.setClientId("测试客户端").setClientSecret("测试客户端").setAllowUrl("*").setContractScope("all");
        SysOAuth2ClientVO newOAuth2Client = oauth2ClientService.newOAuth2Client(sysOAuth2ClientVO);
        LinkedList<Long> newLinkedList = CollUtil.newLinkedList();
        newLinkedList.add(newOAuth2Client.getId());
        List<SysOAuth2ClientVO> sysOAuth2ClientVOS = oauth2ClientService.deleteOAuth2Client(newLinkedList);
        logger.info(">>>>>>>>>>>>>>>>" + sysOAuth2ClientVOS.toString());
        Assertions.assertNotNull(newOAuth2Client);
        Assertions.assertEquals(1, sysOAuth2ClientVOS.size());
    }

    @Test
    @Transactional
    @Rollback
    void modifyOAuth2Client() {
        SysOAuth2ClientVO sysOAuth2ClientVO = new SysOAuth2ClientVO();
        sysOAuth2ClientVO.setClientId("测试客户端").setClientSecret("测试客户端").setAllowUrl("*").setContractScope("all");
        SysOAuth2ClientVO newOAuth2Client = oauth2ClientService.newOAuth2Client(sysOAuth2ClientVO);
        SysOAuth2ClientVO modifyOAuth2Client = new SysOAuth2ClientVO();
        modifyOAuth2Client.setClientId("测试客户端修改").setId(newOAuth2Client.getId());
        SysOAuth2ClientVO afterModificationOAuth2Client = oauth2ClientService.modifyOAuth2Client(modifyOAuth2Client);
        logger.info(">>>>>>>>>>>>>>>>" + afterModificationOAuth2Client.toString());
        Assertions.assertNotNull(newOAuth2Client);
        Assertions.assertEquals("测试客户端修改", afterModificationOAuth2Client.getClientId());
        oauth2ClientCache.REMOVE(afterModificationOAuth2Client.getId());
    }

    @Test
    @Transactional
    @Rollback
    void pagingQueryOAuth2Client() {
        SysOAuth2ClientVO sysOAuth2ClientVO = new SysOAuth2ClientVO();
        sysOAuth2ClientVO.setClientId("测试客户端").setClientSecret("测试客户端").setAllowUrl("*").setContractScope("all");
        SysOAuth2ClientVO newOAuth2Client = oauth2ClientService.newOAuth2Client(sysOAuth2ClientVO);
        IPage<SysOAuth2ClientVO> sysOAuth2ClientVOIPage = oauth2ClientService
                .pagingQueryOAuth2Client(1, 10, new SysOAuth2ClientVO());
        logger.info(">>>>>>>>>>>>>>>>" + sysOAuth2ClientVOIPage.getRecords().toString());
        Assertions.assertNotNull(newOAuth2Client);
        Assertions.assertEquals(2, sysOAuth2ClientVOIPage.getRecords().size());
        oauth2ClientCache.REMOVE(newOAuth2Client.getId());
    }
}