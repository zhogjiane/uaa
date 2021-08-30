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
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.SysFunctionPermissionVO;
import com.conifercone.uaa.service.IFunctionPermissionService;
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
 * 功能权限业务逻辑层单元测试
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@SpringBootTest(classes = UaaApplicationServer.class)
class FunctionPermissionServiceImplTest {

    @Resource
    IFunctionPermissionService functionPermissionService;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "FunctionPermission:")
    @SuppressWarnings("unused")
    private Cache<Long, SysFunctionPermissionVO> functionPermissionCache;

    private static final Logger logger = Logger.getLogger(FunctionPermissionServiceImplTest.class);

    @BeforeEach
    void loginBeforeTesting() {
        MockLogin.loginIn();
    }

    @AfterEach
    void logOutAfterTesting() {
        MockLogin.loginOut();
    }

    /**
     * 新功能权限
     */
    @Test
    @Transactional
    @Rollback
    void newFunctionPermission() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-add");
        sysFunctionPermissionVO.setPermissionName("用户新增");
        sysFunctionPermissionVO.setPermissionScope("user");
        SysFunctionPermissionVO newSysFunctionPermissionVO = functionPermissionService.newFunctionPermission(sysFunctionPermissionVO);
        SysFunctionPermissionVO sysFunctionPermissionVO2 = new SysFunctionPermissionVO();
        sysFunctionPermissionVO2.setPermissionCode("user-add");
        sysFunctionPermissionVO2.setPermissionName("用户新增");
        sysFunctionPermissionVO2.setPermissionScope("user");
        BizException bizException = Assertions.assertThrows(BizException.class, () ->
                functionPermissionService.newFunctionPermission(sysFunctionPermissionVO));
        Assertions.assertEquals(4000, bizException.getCode());
        logger.info(">>>>>>>>>>>>>>>>>>" + newSysFunctionPermissionVO.toString());
        Assertions.assertNotNull(newSysFunctionPermissionVO);
        functionPermissionCache.REMOVE(newSysFunctionPermissionVO.getId());
    }

    /**
     * 删除功能权限
     */
    @Test
    @Transactional
    @Rollback
    void deleteFunctionPermissions() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-del");
        sysFunctionPermissionVO.setPermissionName("用户删除");
        sysFunctionPermissionVO.setPermissionScope("user");
        SysFunctionPermissionVO newSysFunctionPermissionVO = functionPermissionService.newFunctionPermission(sysFunctionPermissionVO);
        LinkedList<Long> idList = CollUtil.newLinkedList();
        idList.add(newSysFunctionPermissionVO.getId());
        List<SysFunctionPermissionVO> sysFunctionPermissionVOS = functionPermissionService.deleteFunctionPermissions(idList);
        logger.info(">>>>>>>>>>>>>>>>>>" + sysFunctionPermissionVOS.toString());
        Assertions.assertNotNull(newSysFunctionPermissionVO);
        Assertions.assertEquals(1, sysFunctionPermissionVOS.size());
    }

    /**
     * 修改功能权限
     */
    @Test
    @Transactional
    @Rollback
    void modifyFunctionPermission() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-modify");
        sysFunctionPermissionVO.setPermissionName("用户修改");
        sysFunctionPermissionVO.setPermissionScope("user");
        SysFunctionPermissionVO newSysFunctionPermissionVO = functionPermissionService.newFunctionPermission(sysFunctionPermissionVO);
        SysFunctionPermissionVO afterModification = new SysFunctionPermissionVO();
        afterModification.setPermissionCode("user-modify");
        afterModification.setPermissionName("用户修改后");
        afterModification.setPermissionScope("user");
        afterModification.setId(newSysFunctionPermissionVO.getId());
        SysFunctionPermissionVO modifyFunctionPermission = functionPermissionService.modifyFunctionPermission(afterModification);
        logger.info(">>>>>>>>>>>>>>>>>>" + modifyFunctionPermission.toString());
        Assertions.assertNotNull(newSysFunctionPermissionVO);
        Assertions.assertEquals("用户修改后", modifyFunctionPermission.getPermissionName());
        functionPermissionCache.REMOVE(modifyFunctionPermission.getId());
    }

    /**
     * 分页查询功能权限
     */
    @Test
    @Transactional
    @Rollback
    void pagingQueryFunctionPermissions() {
        IPage<SysFunctionPermissionVO> sysFunctionPermissionVOIPage = functionPermissionService
                .pagingQueryFunctionPermissions(1, 10, new SysFunctionPermissionVO());
        Assertions.assertNotNull(sysFunctionPermissionVOIPage);
    }
}