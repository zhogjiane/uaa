package com.conifercone.uaa.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统功能权限
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("sys_function_permission")
@ApiModel(value = "系统功能权限")
public class SysFunctionPermission extends BaseEntity {

    @ApiModelProperty(value = "权限编码")
    private String permissionCode;

    @ApiModelProperty(value = "权限名称")
    private String permissionName;

    @ApiModelProperty(value = "权限范围")
    private String permissionScope;
}