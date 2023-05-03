package cn.bugstack.springframework.core.io;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

/**
 * 资源处理接口
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
}
