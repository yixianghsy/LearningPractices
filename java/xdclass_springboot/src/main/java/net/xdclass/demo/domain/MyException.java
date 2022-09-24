package net.xdclass.demo.domain;

/**
 * 功能描述：自定义异常类
 *
 * <p> 创建时间：Apr 25, 2018 12:06:51 AM </p> 
 * <p> 作者：小D课堂</p>
 */
public class MyException extends RuntimeException {

    public MyException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}