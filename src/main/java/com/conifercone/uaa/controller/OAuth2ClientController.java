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

package com.conifercone.uaa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.conifercone.uaa.domain.vo.SysOAuth2ClientVO;
import com.conifercone.uaa.service.IOAuth2ClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * OAuth2客户端
 *
 * @author sky5486560@gmail.com
 * @date 2021/8/21
 */
@RestController
@RequestMapping("/oauth2Client")
@Api(value = "OAuth2客户端", tags = "OAuth2客户端")
public class OAuth2ClientController {

    @Resource
    IOAuth2ClientService oauth2ClientService;


    @PostMapping
    @ApiOperation(value = "新增OAuth2客户端", tags = "OAuth2客户端")
    public SysOAuth2ClientVO newOAuth2Client(@RequestBody @Valid SysOAuth2ClientVO sysOAuth2ClientVO) {
        return oauth2ClientService.newOAuth2Client(sysOAuth2ClientVO);
    }

    @DeleteMapping
    @ApiOperation(value = "删除OAuth2客户端", tags = "OAuth2客户端")
    public List<SysOAuth2ClientVO> deleteOAuth2Client(@RequestBody List<Long> oauth2ClientIdList) {
        return oauth2ClientService.deleteOAuth2Client(oauth2ClientIdList);
    }

    @PutMapping
    @ApiOperation(value = "修改OAuth2客户端", tags = "OAuth2客户端")
    public SysOAuth2ClientVO modifyOAuth2Client(@RequestBody @Valid SysOAuth2ClientVO sysOAuth2ClientVO) {
        return oauth2ClientService.modifyOAuth2Client(sysOAuth2ClientVO);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询OAuth2客户端", tags = "OAuth2客户端")
    public IPage<SysOAuth2ClientVO> pagingQueryOAuth2Client(Integer pageNo, Integer pageSize, SysOAuth2ClientVO sysOAuth2ClientVO) {
        return oauth2ClientService.pagingQueryOAuth2Client(pageNo, pageSize, sysOAuth2ClientVO);
    }
}
