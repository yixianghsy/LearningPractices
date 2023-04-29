package com.mall.utils;

import java.io.Serializable;

public class ResultUtil implements Serializable {

    private String code;

    private String msg;

    private Upload data;

    private int status;

    public ResultUtil() {
    }

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

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public Upload getData() {
        return this.data;
    }

    public int getStatus() {
        return this.status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Upload data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ResultUtil)) return false;
        final ResultUtil other = (ResultUtil) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$code = this.getCode();
        final Object other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) return false;
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null ? other$msg != null : !this$msg.equals(other$msg)) return false;
        final Object this$data = this.getData();
        final Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
        if (this.getStatus() != other.getStatus()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResultUtil;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $code = this.getCode();
        result = result * PRIME + ($code == null ? 43 : $code.hashCode());
        final Object $msg = this.getMsg();
        result = result * PRIME + ($msg == null ? 43 : $msg.hashCode());
        final Object $data = this.getData();
        result = result * PRIME + ($data == null ? 43 : $data.hashCode());
        result = result * PRIME + this.getStatus();
        return result;
    }

    public String toString() {
        return "ResultUtil(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ", status=" + this.getStatus() + ")";
    }
}

