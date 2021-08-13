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

import com.conifercone.uaa.domain.exception.BizException;
import com.conifercone.uaa.domain.vo.ResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Nonnull;

/**
 * 全局处理增强版Controller，避免Controller里返回数据每次都要用响应体来包装
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/13
 */
@RestControllerAdvice(basePackages = {"com.conifercone.uaa.controller"})
public class ResponseControllerAdviceHandler implements ResponseBodyAdvice<Object> {

    /**
     * 接口支持的方法
     *
     * @param returnType    返回类型
     * @param converterType 转换器的类型
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter returnType, @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false
        return !returnType.getGenericParameterType().equals(ResultVO.class);
    }

    /**
     * 在对象响应之前的处理方法
     *
     * @param body                  响应对象
     * @param returnType            返回类型
     * @param selectedContentType   所选内容类型
     * @param selectedConverterType 选择转换类型
     * @param request               请求
     * @param response              响应
     * @return {@link Object}
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, @Nonnull MediaType selectedContentType,
                                  @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @Nonnull ServerHttpRequest request, @Nonnull ServerHttpResponse response) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultVO里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(new ResultVO<>(body));
            } catch (JsonProcessingException e) {
                throw new BizException();
            }
        }
        // 将原本的数据包装在ResultVO里
        return new ResultVO<>(body);
    }
}
