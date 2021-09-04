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
import cn.conifercone.uaa.domain.vo.SysRoleVO;
import cn.conifercone.uaa.service.IRoleService;
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
 * 角色单元测试
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@SpringBootTest(classes = UaaApplicationServer.class)
class RoleServiceImplTest {

    @Resource
    IRoleService roleService;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "Role:")
    @SuppressWarnings("unused")
    private Cache<Long, SysRoleVO> rolesCache;

    private static final Logger logger = Logger.getLogger(RoleServiceImplTest.class);

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
    void newRole() {
        SysRoleVO sysRoleVO = new SysRoleVO();
        sysRoleVO.setRoleCode("admin");
        sysRoleVO.setRoleName("管理员");
        SysRoleVO newRole = roleService.newRole(sysRoleVO);
        logger.info(">>>>>>>>>>>>>>>>" + newRole.toString());
        Assertions.assertNotNull(newRole);
        Assertions.assertEquals("admin", newRole.getRoleCode());
        rolesCache.REMOVE(newRole.getId());
    }

    @Test
    @Transactional
    @Rollback
    void deleteRoles() {
        SysRoleVO sysRoleVO = new SysRoleVO();
        sysRoleVO.setRoleCode("admin");
        sysRoleVO.setRoleName("管理员");
        SysRoleVO newRole = roleService.newRole(sysRoleVO);
        LinkedList<Long> idList = CollUtil.newLinkedList();
        idList.add(newRole.getId());
        List<SysRoleVO> deleteRoles = roleService.deleteRoles(idList);
        logger.info(">>>>>>>>>>>>>>>>" + deleteRoles.toString());
        Assertions.assertNotNull(deleteRoles);
        Assertions.assertEquals(1, deleteRoles.size());
    }

    @Test
    @Transactional
    @Rollback
    void modifyRole() {
        SysRoleVO sysRoleVO = new SysRoleVO();
        sysRoleVO.setRoleCode("admin");
        sysRoleVO.setRoleName("管理员");
        SysRoleVO newRole = roleService.newRole(sysRoleVO);
        SysRoleVO sysRoleVO2 = new SysRoleVO();
        sysRoleVO2.setRoleCode("admin");
        sysRoleVO2.setId(newRole.getId());
        sysRoleVO2.setRoleName("管理员修改后");
        SysRoleVO modifyRole = roleService.modifyRole(sysRoleVO2);
        logger.info(">>>>>>>>>>>>>>>>" + modifyRole.toString());
        Assertions.assertNotNull(modifyRole);
        Assertions.assertEquals("管理员修改后", modifyRole.getRoleName());
        rolesCache.REMOVE(modifyRole.getId());
    }

    @Test
    @Transactional
    @Rollback
    void pagingQueryRole() {
        SysRoleVO sysRoleVO = new SysRoleVO();
        sysRoleVO.setRoleCode("admin");
        sysRoleVO.setRoleName("管理员");
        SysRoleVO newRole = roleService.newRole(sysRoleVO);
        IPage<SysRoleVO> sysRoleVOIPage = roleService.pagingQueryRole(1, 10, new SysRoleVO());
        Assertions.assertNotNull(sysRoleVOIPage.getRecords());
        Assertions.assertEquals(1, sysRoleVOIPage.getRecords().size());
        rolesCache.REMOVE(newRole.getId());
    }
}