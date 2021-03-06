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

package cn.conifercone.uaa.domain.vo;

import com.baomidou.mybatisplus.core.injector.methods.Insert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统角色方法权限（RBAC）
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/30
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统角色方法权限（RBAC）")
public class SysRoleFunctionPermissionVO extends BaseVO implements Serializable {

    private static final long serialVersionUID = 4742086279323459609L;

    @ApiModelProperty(value = "权限id")
    @NotNull(message = "功能权限id不能为空", groups = {Insert.class})
    private Long permissionId;

    @ApiModelProperty(value = "角色id")
    @NotNull(message = "角色id不能为空", groups = {Insert.class})
    private Long roleId;

    @Override
    public String toString() {
        return "SysRoleFunctionPermissionVO{" +
                "id=" + id +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", removed=" + removed +
                ", tenantId=" + tenantId +
                ", permissionId=" + permissionId +
                ", roleId=" + roleId +
                '}';
    }
}
