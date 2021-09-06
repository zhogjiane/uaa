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

package cn.conifercone.uaa.util;

import cn.conifercone.uaa.domain.constant.JwtDataConstant;
import cn.conifercone.uaa.domain.constant.TokenThreadLocal;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import io.jsonwebtoken.Claims;

/**
 * 租户工具类
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
public class TenantUtil {

    private TenantUtil() {
    }

    /**
     * 获取登录用户租户id
     *
     * @return {@link Long}
     */
    public static Long getLoginUserTenantId() {
        String tokenValue;
        try {
            tokenValue = StpUtil.getTokenValue();
        } catch (SaTokenException e) {
            // 尝试从线程变量中获取tokenValue
            tokenValue = TokenThreadLocal.threadLocal.get();
        }
        Claims claims = SaTokenJwtUtil.getClaims(tokenValue);
        return Long.parseLong(String.valueOf(claims.get(JwtDataConstant.TENANT_ID)));
    }
}
