package com.mall.api;

/**
 * webSocket 响应参数
 * @param <T>
 */
public class Result<T> {
    private  long status ;
    private String message;

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Result(long status, String message) {
        this.status = status;
        this.message = message;
    }
}
