package com.mall.mansger.service;
import com.mall.utils.ResultUtil;

import java.io.IOException;

public interface UploadService {

    public ResultUtil getUpToken();

    public ResultUtil uploadFileQiniu(byte[] file, String fileName) throws IOException, Exception;
}
