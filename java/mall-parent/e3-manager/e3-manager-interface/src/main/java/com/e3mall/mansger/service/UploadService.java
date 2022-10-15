package com.e3mall.mansger.service;

import com.e3mall.utils.ResultUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface UploadService {

    public ResultUtil getUpToken();

    public ResultUtil uploadFileQiniu(byte[] file, String fileName) throws IOException, Exception;
}
