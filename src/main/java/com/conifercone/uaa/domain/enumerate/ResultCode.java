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

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.EnumSet;

/**
 * 响应码枚举
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/13
 */
public enum ResultCode {

    //1000系列通用错误
    SUCCESS(1000, "操作成功"),
    FAILED(1001, "接口错误"),
    VALIDATE_FAILED(1002, "参数校验失败"),
    ERROR(1003, "未知错误"),
    DATA_DUPLICATION(1004, "数据重复"),

    //2000系列用户错误
    USER_NOT_EXIST(2000, "用户不存在"),
    USER_LOGIN_FAIL(2001, "用户名或密码错误"),
    USER_NOT_LOGIN(2002, "用户还未登录,请先登录"),
    NO_PERMISSION(2003, "权限不足,请联系管理员"),

    //3000系列客户端错误
    CLIENT_NOT_EXIST(3000, "客户端不存在"),

    //4000系列功能权限异常
    DUPLICATE_FUNCTION_PERMISSION_CODE(4000, "功能权限编码重复"),
    DUPLICATE_FUNCTION_PERMISSION_NAME(4001, "功能权限名称重复");

    private final int code;
    private final String msg;
    private MessageSource messageSource;

    private void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return messageSource.getMessage(String.valueOf(code), null, msg, LocaleContextHolder.getLocale());
    }

    //经过静态内部类的方式注入bean，并赋值到枚举中
    @Component
    public static class ReportTypeServiceInjector {

        @Resource
        private MessageSource messageSource;

        @PostConstruct
        public void postConstruct() {
            for (ResultCode rt : EnumSet.allOf(ResultCode.class))
                rt.setMessageSource(messageSource);
        }
    }

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
