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
-- auto-generated definition
create schema uaa collate utf8_general_ci;

create table sys_oauth2_client
(
    id bigint(40) not null comment '主键id'
        primary key,
    client_id varchar(100) null comment '客户端id',
    client_secret varchar(100) null comment '客户端密钥',
    allow_url varchar(100) null comment '允许访问的地址',
    contract_scope varchar(100) null comment '契约范围',
    create_by bigint(40) null comment '创建人',
    create_time timestamp null comment '创建时间',
    update_by bigint(40) null comment '更新人',
    update_time timestamp null comment '更新时间',
    removed tinyint null comment '是否删除'
)
    comment 'Oauth2客户端信息表';

create table sys_user
(
    id bigint(40) not null
        primary key,
    account_name varchar(100) null comment '账户名称',
    password varchar(100) null comment '密码',
    real_name varchar(100) null comment '真实名称',
    sex char(10) null comment '性别',
    phone_number varchar(20) null comment '手机号',
    email varchar(50) null comment '邮箱',
    create_by bigint(40) null comment '创建人',
    create_time timestamp not null on update CURRENT_TIMESTAMP comment '创建时间',
    update_by bigint(40) null comment '更新人',
    update_time timestamp not null comment '更新时间',
    removed tinyint null comment '是否已删除'
)
    comment '系统用户表';

