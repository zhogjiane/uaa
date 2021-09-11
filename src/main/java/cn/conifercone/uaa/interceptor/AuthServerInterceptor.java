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

package cn.conifercone.uaa.interceptor;

import cn.conifercone.uaa.domain.constant.TokenThreadLocal;
import cn.conifercone.uaa.domain.enumerate.ResultCode;
import cn.conifercone.uaa.domain.exception.BizException;
import cn.hutool.core.text.CharSequenceUtil;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.stereotype.Component;

/**
 * grpc鉴权服务拦截器
 *
 * @author sky5486560@gmail.com
 * @date 2021/9/6
 */
@Slf4j
@GrpcGlobalServerInterceptor
@Component
public class AuthServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        //获取客户端参数
        log.info("header received from client:" + metadata);
        Metadata.Key<String> token = Metadata.Key.of("token", Metadata.ASCII_STRING_MARSHALLER);
        String tokenStr = metadata.get(token);
        if (CharSequenceUtil.isBlank(tokenStr)) {
            serverCall.close(Status.DATA_LOSS, metadata);
            throw new BizException(ResultCode.USER_NOT_LOGIN);
        }
        TokenThreadLocal.threadLocal.set(tokenStr);
        //服务端写回参数
        ServerCall<ReqT, RespT> call = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
            @Override
            public void sendHeaders(Metadata headers) {
                //服务端写回参数的同时清理ThreadLocal
                TokenThreadLocal.threadLocal.remove();
                //由于grpc服务是全双工的,因此我们也可以向客户端推送 Metadata 数据
                headers.put(token, tokenStr);
                super.sendHeaders(headers);
            }
        };
        return serverCallHandler.startCall(call, metadata);
    }
}
