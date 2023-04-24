package com.mall.utils;

import lombok.*;

import java.io.Serializable;

@Data
public class ResultUtil implements Serializable {

    private String code;

    private String msg;

    private Upload data;

    private  int status;

    public static ResultUtil success(Upload data) {
        return resultData(CodeEnum.SUCCESS.val(), CodeEnum.SUCCESS.msg(), data);
    }

    public static ResultUtil success(Upload data, String msg) {
        return resultData(CodeEnum.SUCCESS.val(), msg, data);
    }

    public static ResultUtil fail(String code, String msg) {
        return resultData(code, msg, null);
    }

    public static ResultUtil fail(String code, String msg, Upload data) {
        return resultData(code, msg, data);
    }

    private static ResultUtil resultData(String code, String msg, Upload data) {
        ResultUtil resultData = new ResultUtil();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setData(data);
        return resultData;
    }


    public void setstatus(int i) {
    }
}

