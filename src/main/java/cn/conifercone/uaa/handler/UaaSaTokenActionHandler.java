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

package cn.conifercone.uaa.handler;

import cn.conifercone.uaa.domain.constant.JwtDataConstant;
import cn.conifercone.uaa.domain.entity.SysUser;
import cn.conifercone.uaa.mapper.UserMapper;
import cn.conifercone.uaa.util.SaTokenJwtUtil;
import cn.dev33.satoken.action.SaTokenActionDefaultImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 继承Sa-Token行为Bean默认实现, 重写部分逻辑
 *
 * @author sky5486560@gmail.com
 * @date 2021/9/6
 */
@Component
public class UaaSaTokenActionHandler extends SaTokenActionDefaultImpl {

    @Resource
    UserMapper userMapper;

    @Override
    public String createToken(Object loginId, String loginType) {
        SysUser sysUser = userMapper.queryUsersBasedOnId(Long.parseLong(String.valueOf(loginId)));
        Map<String, Object> map = new HashMap<>();
        map.put(JwtDataConstant.TENANT_ID, String.valueOf(sysUser.getTenantId()));
        map.put(JwtDataConstant.DATA_PERMISSIONS, sysUser.getDataPermissions().getCode());
        return SaTokenJwtUtil.createToken(loginId, map);
    }
}
