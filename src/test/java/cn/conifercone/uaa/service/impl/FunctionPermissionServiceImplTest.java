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

import cn.conifercone.uaa.UaaApplicationServer;
import cn.conifercone.uaa.domain.exception.BizException;
import cn.conifercone.uaa.domain.vo.SysFunctionPermissionVO;
import cn.conifercone.uaa.service.IFunctionPermissionService;
import cn.conifercone.uaa.util.MockLogin;
import cn.hutool.core.collection.CollUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * ???????????????????????????????????????
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
     * ???????????????
     */
    @Test
    @Transactional
    @Rollback
    void newFunctionPermission() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-add");
        sysFunctionPermissionVO.setPermissionName("????????????");
        sysFunctionPermissionVO.setPermissionScope("user");
        SysFunctionPermissionVO newSysFunctionPermissionVO = functionPermissionService.newFunctionPermission(sysFunctionPermissionVO);
        SysFunctionPermissionVO sysFunctionPermissionVO2 = new SysFunctionPermissionVO();
        sysFunctionPermissionVO2.setPermissionCode("user-add");
        sysFunctionPermissionVO2.setPermissionName("????????????");
        sysFunctionPermissionVO2.setPermissionScope("user");
        BizException bizException = Assertions.assertThrows(BizException.class, () ->
                functionPermissionService.newFunctionPermission(sysFunctionPermissionVO));
        Assertions.assertEquals(4000, bizException.getCode());
        logger.info(">>>>>>>>>>>>>>>>>>" + newSysFunctionPermissionVO.toString());
        Assertions.assertNotNull(newSysFunctionPermissionVO);
        functionPermissionCache.REMOVE(newSysFunctionPermissionVO.getId());
    }

    /**
     * ??????????????????
     */
    @Test
    @Transactional
    @Rollback
    void deleteFunctionPermissions() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-del");
        sysFunctionPermissionVO.setPermissionName("????????????");
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
     * ??????????????????
     */
    @Test
    @Transactional
    @Rollback
    void modifyFunctionPermission() {
        SysFunctionPermissionVO sysFunctionPermissionVO = new SysFunctionPermissionVO();
        sysFunctionPermissionVO.setPermissionCode("user-modify");
        sysFunctionPermissionVO.setPermissionName("????????????");
        sysFunctionPermissionVO.setPermissionScope("user");
        SysFunctionPermissionVO newSysFunctionPermissionVO = functionPermissionService.newFunctionPermission(sysFunctionPermissionVO);
        SysFunctionPermissionVO afterModification = new SysFunctionPermissionVO();
        afterModification.setPermissionCode("user-modify");
        afterModification.setPermissionName("???????????????");
        afterModification.setPermissionScope("user");
        afterModification.setId(newSysFunctionPermissionVO.getId());
        SysFunctionPermissionVO modifyFunctionPermission = functionPermissionService.modifyFunctionPermission(afterModification);
        logger.info(">>>>>>>>>>>>>>>>>>" + modifyFunctionPermission.toString());
        Assertions.assertNotNull(newSysFunctionPermissionVO);
        Assertions.assertEquals("???????????????", modifyFunctionPermission.getPermissionName());
        functionPermissionCache.REMOVE(modifyFunctionPermission.getId());
    }

    /**
     * ????????????????????????
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