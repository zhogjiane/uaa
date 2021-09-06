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

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * Jwt工具类
 *
 * @author sky5486560@gmail.com
 * @date 2021/9/6
 */
@SuppressWarnings("unused")
public class SaTokenJwtUtil {

    private SaTokenJwtUtil() {
    }

    /**
     * 秘钥 (随便手打几个字母就好了)
     */
    public static final String BASE64_SECURITY = "79e7c69681b8270162386e6daa53d1dd";

    /**
     * token有效期 (单位: 秒)
     */
    public static final long TIMEOUT = 3600;


    public static final String LOGIN_ID_KEY = "loginId";


    /**
     * 根据userId生成token
     *
     * @param loginId 账号id
     * @return jwt-token
     */
    public static String createToken(Object loginId, Map<String, Object> map) {
        // 在这里你可以使用官方提供的claim方法构建载荷，也可以使用setPayload自定义载荷，但是两者不可一起使用
        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setId(String.valueOf(loginId))
                .claim(LOGIN_ID_KEY, String.valueOf(loginId))
                .setIssuedAt(new Date())    // 签发日期
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * TIMEOUT))  // 有效截止日期
                .signWith(SignatureAlgorithm.HS256, BASE64_SECURITY.getBytes()); // 加密算法
        if (MapUtil.isNotEmpty(map)) {
            map.forEach(builder::claim);
        }
        //生成JWT
        return builder.compact();
    }

    /**
     * 从一个jwt里面解析出Claims
     *
     * @param tokenValue token值
     * @return Claims对象
     */
    public static Claims getClaims(String tokenValue) {
        return Jwts.parser()
                .setSigningKey(BASE64_SECURITY.getBytes())
                .parseClaimsJws(tokenValue).getBody();
    }

    /**
     * 从一个jwt里面解析loginId
     *
     * @param tokenValue token值
     * @return loginId
     */
    public static String getLoginId(String tokenValue) {
        try {
            Object loginId = getClaims(tokenValue).get(LOGIN_ID_KEY);
            if (loginId == null) {
                return null;
            }
            return String.valueOf(loginId);
        } catch (ExpiredJwtException e) {
            return NotLoginException.TOKEN_TIMEOUT;
        } catch (MalformedJwtException e) {
            throw NotLoginException.newInstance(StpUtil.stpLogic.loginType, NotLoginException.INVALID_TOKEN);
        } catch (Exception e) {
            throw new SaTokenException(e);
        }
    }
}
