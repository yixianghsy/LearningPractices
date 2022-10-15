package com.qiniu.service;

import com.qiniu.common.ResultUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UploadService {

    public ResultUtil getUpToken();

    public ResultUtil uploadFileQiniu(MultipartFile file, HttpServletRequest request) throws IOException, Exception;
}
