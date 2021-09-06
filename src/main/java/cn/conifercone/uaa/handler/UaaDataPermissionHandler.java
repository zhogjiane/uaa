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

import cn.conifercone.uaa.domain.constant.TokenThreadLocal;
import cn.conifercone.uaa.domain.enumerate.ResultCode;
import cn.conifercone.uaa.domain.exception.BizException;
import cn.conifercone.uaa.util.DataPermissionsUtil;
import cn.conifercone.uaa.util.SaTokenJwtUtil;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;

/**
 * 数据权限处理器
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/24
 */
@Slf4j
@SuppressWarnings("unused")
public class UaaDataPermissionHandler implements DataPermissionHandler {

    /**
     * 获得sql
     *
     * @param where             where条件
     * @param mappedStatementId 映射语句id
     * @return {@link Expression}
     */
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        Integer loginUserDataPermissions = DataPermissionsUtil.getLoginUserDataPermissions();
        Expression expression = new HexValue(" 1 = 1 ");
        if (where == null) {
            where = expression;
        }
        switch (loginUserDataPermissions) {
            // 查看全部
            case 1:
                return where;
            // 查看自己的数据
            case 3:
                String tokenValue;
                try {
                    tokenValue = StpUtil.getTokenValue();
                } catch (SaTokenException e) {
                    // 尝试从线程变量中获取tokenValue
                    tokenValue = TokenThreadLocal.threadLocal.get();
                }
                EqualsTo selfEqualsTo = new EqualsTo();
                selfEqualsTo.setLeftExpression(new Column("create_by"));
                String loginId = SaTokenJwtUtil.getLoginId(tokenValue);
                if (CharSequenceUtil.isBlank(loginId)) {
                    throw new BizException(ResultCode.USER_NOT_LOGIN);
                }
                selfEqualsTo.setRightExpression(new LongValue(loginId));
                return new AndExpression(where, selfEqualsTo);
            default:
                break;
        }
        return where;
    }
}
