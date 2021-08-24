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

package com.conifercone.uaa.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.conifercone.uaa.domain.constant.TenantConstant;
import com.conifercone.uaa.util.TenantUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.Arrays;

/**
 * uaa多租户处理器
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
public class UaaTenantLineHandler implements TenantLineHandler {


    /**
     * 获取租户id
     *
     * @return {@link Expression}
     */
    @Override
    public Expression getTenantId() {
        return new LongValue(TenantUtil.getLoginUserTenantId());
    }

    /**
     * 忽略表
     *
     * @param tableName 表名
     * @return boolean
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return Arrays.stream(TenantConstant.IGNORE_TENANT_TABLES).anyMatch(e -> e.equalsIgnoreCase(tableName));
    }
}
