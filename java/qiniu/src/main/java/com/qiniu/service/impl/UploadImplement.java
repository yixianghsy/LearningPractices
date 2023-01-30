package com.qiniu.service.impl;

import com.qiniu.common.QiniuCloudUtil;
import com.qiniu.common.ResultUtil;
import com.qiniu.common.Upload;
import com.qiniu.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
public class UploadImplement  implements UploadService {
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
    public ResultUtil uploadFileQiniu(MultipartFile file, HttpServletRequest request) throws Exception {
        ResultUtil apiresult = new ResultUtil();
        byte[] uploadBytes = file.getBytes();
        //文件名
        String fileName = file.getOriginalFilename();
        //上传成功后返回的URL
        String fileUrl = QiniuCloudUtil.uploadFileBytes(uploadBytes, fileName);

        //普通方式上传文件
        System.out.println(fileName);
        Upload upload = new Upload();
        upload.setFileUrl(fileUrl);
        System.out.println(fileUrl);
        apiresult.setData(upload);
        apiresult.setstatus(upload!=null?1:0);
        apiresult.setMsg(upload!=null?"成功":"失败");
        return apiresult;
    }
}
