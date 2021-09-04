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

package cn.conifercone.uaa.configurations;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * jackson全局配置
 * <p>
 * <b>1:将Long类型转换成string类型返回，避免大整数导致前端精度丢失的问题<b/>
 * <br/>
 * <b>2:将LocalDateTime全局返回时间戳（方便前端处理）并且将参数里面的时间戳转换成LocalDateTime<b/>
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@Configuration
public class JacksonCustomizerConfig {

    /**
     * 统一配置类型的转换策略
     *
     * @return {@link Jackson2ObjectMapperBuilderCustomizer}
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            //将Long类型转换成string类型返回，避免大整数导致前端精度丢失的问题
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            //将LocalDateTime全局返回时间戳（方便前端处理）并且将参数里面的时间戳转换成LocalDateTime
            builder.serializerByType(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
            builder.deserializerByType(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        };
    }
}
