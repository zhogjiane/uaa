package com.conifercone.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.conifercone.uaa.domain.entity.SysFunctionPermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 功能权限mapper
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/25
 */
@Mapper
public interface FunctionPermissionMapper extends BaseMapper<SysFunctionPermission> {
}