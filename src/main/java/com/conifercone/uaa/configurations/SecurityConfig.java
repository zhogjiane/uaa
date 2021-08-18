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

package com.conifercone.uaa.configurations;

import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conifercone.uaa.domain.entity.SysUser;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * 安全配置类
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
@Configuration
public class SecurityConfig {

    @Resource
    IUserService userService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
     * 注入BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Sa-OAuth2 定制化配置
    @Autowired
    public void setSaOAuth2Config(SaOAuth2Config cfg) {
        cfg.setDoLoginHandle((name, pwd) -> {
            LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserLambdaQueryWrapper.eq(SysUser::getAccountName, name);
            SysUser sysUser = userService.getOne(sysUserLambdaQueryWrapper);
            if (ObjectUtil.isEmpty(sysUser)) {
                throw new BizException(ResultCode.USER_NOT_EXIST);
            }
            if (bCryptPasswordEncoder.matches(pwd, sysUser.getPassword())) {
                StpUtil.login(sysUser.getId());
                return SaResult.ok();
            }
            throw new BizException(ResultCode.USER_LOGIN_FAIL);
        });
    }
}
