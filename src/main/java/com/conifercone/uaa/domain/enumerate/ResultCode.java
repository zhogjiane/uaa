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

package com.conifercone.uaa.domain.enumerate;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/13
 */
@Getter
public enum ResultCode {

    //1000系列通用错误
    SUCCESS(1000, "操作成功"),
    FAILED(1001, "接口错误"),
    VALIDATE_FAILED(1002, "参数校验失败"),
    ERROR(1003, "未知错误"),

    //2000系列用户错误
    USER_NOT_EXIST(2000, "用户不存在"),
    USER_LOGIN_FAIL(2001, "用户名或密码错误"),
    USER_NOT_LOGIN(2002, "用户还未登录,请先登录"),
    NO_PERMISSION(2003, "权限不足,请联系管理员");

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}