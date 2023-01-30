package com.qiniu.controller;

import com.qiniu.common.ResultUtil;
import com.qiniu.service.UploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/Upload")
public class UploadController {
    @Resource
    private UploadService uploadService;

    @RequestMapping(value = "/getQintuToken", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取七牛云Token", notes = "获取七牛云Token")
    public ResultUtil getUpToken(
            @ApiParam(value = "hsymall", required = false) @RequestParam(required = false) String bucket
    ) {
        ResultUtil resultUtil = new ResultUtil();
        resultUtil = uploadService.getUpToken();
        return resultUtil;
    }

    @RequestMapping(value = "/uploadFileQiniu", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "上传文件到七牛云", notes = "上传文件到七牛云")
    public ResultUtil uploadQiniu(
            @ApiParam(value = "file", required = false) @RequestParam(required = false) MultipartFile file,
            HttpServletRequest request
    ) throws Exception {
        ResultUtil resultUtil = new ResultUtil();
        if (file.isEmpty()) {
            resultUtil.setMsg("文件为空，请重新上传");
            return resultUtil;
        }
        resultUtil = uploadService.uploadFileQiniu(file,request);
        System.out.println(resultUtil.getData().getFileUrl());
        return  resultUtil;
    }


}
