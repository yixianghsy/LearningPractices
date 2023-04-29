package com.mall.mansger.service.impl;

import com.mall.mansger.service.UploadService;
import com.mall.utils.QiniuCloudUtil;
import com.mall.utils.ResultUtil;
import com.mall.utils.Upload;
import org.apache.dubbo.config.annotation.Service;

@Service
public class UploadImplement implements UploadService {
    @Override
    public ResultUtil getUpToken() {
        ResultUtil apiresult = new ResultUtil();
        String upToken= QiniuCloudUtil.getupToken();
        Upload upload = new Upload();
        upload.setToken(upToken);
        apiresult.setData(upload);
        apiresult.setstatus(upload!=null?1:0);
        apiresult.setMsg(upload!=null?"成功" : "失败");
        return apiresult;
    }

    @Override
    public ResultUtil uploadFileQiniu(byte[] uploadBytes,String fileName) throws Exception {
        ResultUtil apiresult = new ResultUtil();
//        byte[] uploadBytes = file.getBytes();
        //上传成功后返回的URL
        String fileUrl = QiniuCloudUtil.uploadFileBytes(uploadBytes, fileName);
        //普通方式上传文件
        System.out.println(fileName);
        Upload upload = new Upload();
        upload.setFileUrl(fileUrl);
        apiresult.setData(upload);
        apiresult.setstatus(upload!=null?1:0);
        apiresult.setMsg(upload!=null?"成功":"失败");
        return apiresult;
    }
}