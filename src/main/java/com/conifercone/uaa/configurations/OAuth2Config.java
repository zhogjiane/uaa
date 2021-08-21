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
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conifercone.uaa.domain.constant.UserSessionDataNameConstant;
import com.conifercone.uaa.domain.entity.SysUser;
import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * SaOAuth2配置文件
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/18
 */
@Configuration
@Slf4j
public class OAuth2Config {

    @Resource
    IUserService userService;

    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;


    // Sa-OAuth2 定制化配置
    @Autowired
    public void setSaOAuth2Config(SaOAuth2Config cfg) {
        //设置登录验证逻辑
        cfg.setDoLoginHandle((name, pwd) -> {
            LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysUserLambdaQueryWrapper.eq(SysUser::getAccountName, name);
            SysUser sysUser = userService.getOne(sysUserLambdaQueryWrapper);
            if (ObjectUtil.isEmpty(sysUser)) {
                throw new BizException(ResultCode.USER_NOT_EXIST);
            }
            if (bCryptPasswordEncoder.matches(pwd, sysUser.getPassword())) {
                StpUtil.login(sysUser.getId());
                //自定义用户session数据
                SaSession sessionByLoginId = StpUtil.getSessionByLoginId(sysUser.getId());
                sessionByLoginId.set(UserSessionDataNameConstant.TENANT_ID, sysUser.getTenantId());
                return name;
            }
            throw new BizException(ResultCode.USER_LOGIN_FAIL);
        });
    }
}
