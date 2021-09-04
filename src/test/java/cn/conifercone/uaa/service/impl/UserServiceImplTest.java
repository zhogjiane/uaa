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
import cn.conifercone.uaa.domain.enumerate.DataPermissions;
import cn.conifercone.uaa.domain.enumerate.UserSex;
import cn.conifercone.uaa.domain.vo.SysUserRoleVO;
import cn.conifercone.uaa.domain.vo.SysUserVO;
import cn.conifercone.uaa.service.IUserRoleService;
import cn.conifercone.uaa.service.IUserService;
import cn.conifercone.uaa.util.MockLogin;
import cn.hutool.core.collection.CollUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户单元测试
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@SpringBootTest(classes = UaaApplicationServer.class)
class UserServiceImplTest {

    @Resource
    IUserService userService;

    @Resource
    IUserRoleService userRoleService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "User:")
    @SuppressWarnings("unused")
    private Cache<Long, SysUserVO> userCache;

    @CreateCache(expire = 100, localExpire = 100, cacheType = CacheType.BOTH, name = "UserRole:")
    @SuppressWarnings("unused")
    private Cache<Long, SysUserRoleVO> userRoleCache;

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
    void newUsers() {
        SysUserVO sysUser = new SysUserVO();
        sysUser.setAccountName("test");
        sysUser.setPassword(bCryptPasswordEncoder.encode("test"));
        sysUser.setEmail("sky5486560@gmail.com");
        sysUser.setPhoneNumber("13021705689");
        sysUser.setDataPermissions(DataPermissions.SELF);
        sysUser.setRealName("测试用户");
        sysUser.setSex(UserSex.WOMAN);
        LinkedList<Long> roleIds = CollUtil.newLinkedList();
        roleIds.add(1432233860585578496L);
        sysUser.setRoleIds(roleIds);
        SysUserVO sysUserVO = userService.newUsers(sysUser);
        Assertions.assertNotNull(sysUserVO);
        Long sysUserVOId = sysUserVO.getId();
        List<SysUserRoleVO> sysUserRoleVOS = userRoleService.queryUserRoleRelationshipBasedOnUserId(sysUserVOId);
        List<Long> sysUserRoleIdList = sysUserRoleVOS.stream().map(SysUserRoleVO::getId).collect(Collectors.toList());
        userCache.REMOVE(sysUserVOId);
        userRoleCache.REMOVE_ALL(new HashSet<>(sysUserRoleIdList));
    }

    @Test
    @Transactional
    @Rollback
    void deleteUsers() {
        SysUserVO sysUser = new SysUserVO();
        sysUser.setAccountName("test");
        sysUser.setPassword(bCryptPasswordEncoder.encode("test"));
        sysUser.setEmail("sky5486560@gmail.com");
        sysUser.setPhoneNumber("13021705689");
        sysUser.setDataPermissions(DataPermissions.SELF);
        sysUser.setRealName("测试用户");
        sysUser.setSex(UserSex.WOMAN);
        LinkedList<Long> roleIds = CollUtil.newLinkedList();
        roleIds.add(1432233860585578496L);
        sysUser.setRoleIds(roleIds);
        SysUserVO sysUserVO = userService.newUsers(sysUser);
        LinkedList<Long> list = CollUtil.newLinkedList();
        list.add(sysUserVO.getId());
        List<SysUserVO> sysUserVOS = userService.deleteUsers(list);
        Assertions.assertNotNull(sysUserVOS);
        Assertions.assertEquals(1, sysUserVOS.size());
    }

    @Test
    @Transactional
    @Rollback
    void modifyUser() {
        SysUserVO sysUser = new SysUserVO();
        sysUser.setAccountName("test");
        sysUser.setPassword(bCryptPasswordEncoder.encode("test"));
        sysUser.setEmail("sky5486560@gmail.com");
        sysUser.setPhoneNumber("13021705689");
        sysUser.setDataPermissions(DataPermissions.SELF);
        sysUser.setRealName("测试用户");
        sysUser.setSex(UserSex.WOMAN);
        LinkedList<Long> roleIds = CollUtil.newLinkedList();
        roleIds.add(1432233860585578496L);
        sysUser.setRoleIds(roleIds);
        SysUserVO sysUserVO = userService.newUsers(sysUser);
        SysUserVO modifyUser = new SysUserVO();
        modifyUser.setId(sysUserVO.getId());
        modifyUser.setRealName("测试用户修改");
        SysUserVO modifyUser1 = userService.modifyUser(modifyUser);
        Assertions.assertNotNull(modifyUser1);
        Assertions.assertEquals("测试用户修改", modifyUser1.getRealName());
        Long sysUserVOId = modifyUser1.getId();
        List<SysUserRoleVO> sysUserRoleVOS = userRoleService.queryUserRoleRelationshipBasedOnUserId(sysUserVOId);
        List<Long> sysUserRoleIdList = sysUserRoleVOS.stream().map(SysUserRoleVO::getId).collect(Collectors.toList());
        userCache.REMOVE(sysUserVOId);
        userRoleCache.REMOVE_ALL(new HashSet<>(sysUserRoleIdList));
    }

    @Test
    @Transactional
    @Rollback
    void queryUsersByPagination() {
        IPage<SysUserVO> sysUserVOIPage = userService.queryUsersByPagination(1, 10, new SysUserVO());
        Assertions.assertNotNull(sysUserVOIPage);
    }
}