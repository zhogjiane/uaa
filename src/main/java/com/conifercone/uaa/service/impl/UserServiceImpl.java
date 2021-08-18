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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conifercone.uaa.domain.entity.SysUser;
import com.conifercone.uaa.domain.vo.SysUserVO;
import com.conifercone.uaa.mapper.UserMapper;
import com.conifercone.uaa.service.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 用户service实现
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
@Service
@Slf4j
@Api(value = "用户管理", tags = "用户管理")
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 新增用户
     *
     * @param newUser 新用户
     * @return {@link SysUserVO}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUserVO newUsers(SysUserVO newUser) {
        //密码加密
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        this.save(BeanUtil.copyProperties(newUser, SysUser.class));
        //去除密码等敏感信息
        newUser.setPassword("");
        return newUser;
    }
}
