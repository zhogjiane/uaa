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

package cn.conifercone.uaa.domain.exception;

import cn.conifercone.uaa.domain.enumerate.ResultCode;
import lombok.Getter;

/**
 * 通用业务异常
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/13
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 异常码
     */
    private final int code;

    /**
     * 异常信息
     */
    private final String msg;

    /**
     * 异常数据
     */
    private final String data;

    /**
     * 结果代码
     */
    private final ResultCode resultCode;

    public BizException() {
        this(ResultCode.FAILED);
    }

    public BizException(ResultCode failed) {
        this.code = failed.getCode();
        this.msg = failed.getMsg();
        this.resultCode = failed;
        this.data = null;
    }

    public <T> BizException(ResultCode failed, T data) {
        this.code = failed.getCode();
        this.msg = failed.getMsg();
        this.resultCode = failed;
        this.data = String.valueOf(data);
    }
}
