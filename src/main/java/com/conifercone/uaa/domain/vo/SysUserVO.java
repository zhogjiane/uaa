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

package com.conifercone.uaa.domain.vo;

import com.baomidou.mybatisplus.core.injector.methods.Insert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 用户值对象
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户值对象")
public class SysUserVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 1312152314252356342L;

    @ApiModelProperty(value = "账户名称")
    @NotBlank(message = "账户名称不能为空", groups = {Insert.class})
    @NotNull(message = "账户名称不能为空", groups = {Insert.class})
    private String accountName;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空", groups = {Insert.class})
    @NotNull(message = "密码不能为空", groups = {Insert.class})
    private String password;

    @ApiModelProperty(value = "真实姓名")
    @NotBlank(message = "真实姓名不能为空", groups = {Insert.class})
    @NotNull(message = "真实姓名不能为空", groups = {Insert.class})
    private String realName;

    @ApiModelProperty(value = "性别")
    @NotBlank(message = "性别不能为空", groups = {Insert.class})
    @NotNull(message = "性别不能为空", groups = {Insert.class})
    private String sex;

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "手机号码不能为空", groups = {Insert.class})
    @NotNull(message = "手机号码不能为空", groups = {Insert.class})
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号格式有误")
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式错误")
    private String email;
}
