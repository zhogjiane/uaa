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
/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3306
 Source Schema         : uaa

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 25/08/2021 11:33:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_function_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_function_permission`;
CREATE TABLE `sys_function_permission`
(
    `id`              bigint(40)                                              NOT NULL COMMENT '主键id',
    `permission_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限编码',
    `permission_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
    `create_by`       bigint(40)                                              NULL DEFAULT NULL COMMENT '创建人',
    `create_time`     timestamp                                               NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       bigint(40)                                              NULL DEFAULT NULL COMMENT '更新人',
    `update_time`     timestamp                                               NULL DEFAULT NULL COMMENT '更新时间',
    `removed`         tinyint(4)                                              NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id`       bigint(40)                                              NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_oauth2_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth2_client`;
CREATE TABLE `sys_oauth2_client`
(
    `id`             bigint(40)                                              NOT NULL COMMENT '主键id',
    `client_id`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端id',
    `client_secret`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端密钥',
    `allow_url`      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '允许访问的地址',
    `contract_scope` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '契约范围',
    `create_by`      bigint(40)                                              NULL DEFAULT NULL COMMENT '创建人',
    `create_time`    timestamp                                               NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`      bigint(40)                                              NULL DEFAULT NULL COMMENT '更新人',
    `update_time`    timestamp                                               NULL DEFAULT NULL COMMENT '更新时间',
    `removed`        tinyint(4)                                              NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id`      bigint(40)                                              NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = 'Oauth2客户端信息表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint(40)                                              NOT NULL COMMENT '主键id',
    `role_code`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
    `role_name`   varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
    `create_by`   bigint(40)                                              NULL DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp                                               NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   bigint(40)                                              NULL DEFAULT NULL COMMENT '更新人',
    `update_time` timestamp                                               NULL DEFAULT NULL COMMENT '更新时间',
    `removed`     tinyint(4)                                              NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id`   bigint(40)                                              NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`
(
    `id`             bigint(40)                                              NOT NULL COMMENT '主键id',
    `tenant_name`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户名称',
    `tenant_profile` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户简介',
    `create_by`      bigint(40)                                              NULL DEFAULT NULL COMMENT '创建人',
    `create_time`    timestamp                                               NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      bigint(40)                                              NULL DEFAULT NULL COMMENT '更新人',
    `update_time`    timestamp                                               NULL DEFAULT NULL COMMENT '更新时间',
    `removed`        tinyint(4)                                              NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id`      bigint(40)                                              NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '系统租户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`               bigint(40)                                              NOT NULL,
    `account_name`     varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账户名称',
    `password`         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
    `real_name`        varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实名称',
    `sex`              char(10) CHARACTER SET utf8 COLLATE utf8_general_ci     NULL DEFAULT NULL COMMENT '性别',
    `phone_number`     varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '手机号',
    `email`            varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '邮箱',
    `create_by`        bigint(40)                                              NULL DEFAULT NULL COMMENT '创建人',
    `create_time`      timestamp                                               NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`        bigint(40)                                              NULL DEFAULT NULL COMMENT '更新人',
    `update_time`      timestamp                                               NULL DEFAULT NULL COMMENT '更新时间',
    `removed`          tinyint(4)                                              NULL DEFAULT 0 COMMENT '是否已删除',
    `tenant_id`        bigint(40)                                              NULL DEFAULT NULL COMMENT '租户id',
    `data_permissions` int(50)                                                 NULL DEFAULT NULL COMMENT '用户数据权限范围',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint(40) NOT NULL COMMENT '主键id',
    `role_id`     bigint(40) NULL DEFAULT NULL COMMENT '角色id',
    `user_id`     bigint(40) NULL DEFAULT NULL COMMENT '用户id',
    `create_by`   bigint(40) NULL DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp  NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   bigint(40) NULL DEFAULT NULL COMMENT '更新人',
    `update_time` timestamp  NULL DEFAULT NULL COMMENT '更新时间',
    `removed`     tinyint(4) NULL DEFAULT 0 COMMENT '是否删除',
    `tenant_id`   bigint(40) NULL DEFAULT NULL COMMENT '租户id',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `FK_Reference_1` (`user_id`) USING BTREE,
    INDEX `FK_Reference_2` (`role_id`) USING BTREE,
    CONSTRAINT `FK_Reference_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT `FK_Reference_2` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
