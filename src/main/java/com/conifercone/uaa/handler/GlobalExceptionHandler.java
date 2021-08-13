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

import com.conifercone.uaa.domain.enumerate.ResultCode;
import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理类
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/13
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理程序
     *
     * @param bizException 业务异常
     * @return {@link ResultVO}<{@link Object}>
     */
    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Object> bizExceptionHandler(BizException bizException) {
        return new ResultVO<>(ResultCode.FAILED, bizException.getMsg());
    }

    /**
     * 方法参数不是有效的异常处理程序
     *
     * @param methodArgumentNotValidException 方法参数无效异常
     * @return {@link ResultVO}<{@link Object}>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("方法参数错误异常");
        List<String> list = new ArrayList<>();
        // 从异常对象中拿到ObjectError对象
        if (!methodArgumentNotValidException.getBindingResult().getAllErrors().isEmpty()) {
            for (ObjectError error : methodArgumentNotValidException.getBindingResult().getAllErrors()) {
                list.add(error.getDefaultMessage());
            }
        }
        // 然后提取错误提示信息进行返回
        return new ResultVO<>(ResultCode.VALIDATE_FAILED, list);
    }
}