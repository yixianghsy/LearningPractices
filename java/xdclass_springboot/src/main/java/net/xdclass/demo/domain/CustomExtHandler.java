package net.xdclass.demo.domain;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class CustomExtHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomExtHandler.class);

	
	//捕获全局异常,处理所有不可知的异常
	@ExceptionHandler(value=Exception.class)
	//@ResponseBody
    Object handleException(Exception e,HttpServletRequest request){
		LOG.error("url {}, msg {}",request.getRequestURL(), e.getMessage()); 
		Map<String, Object> map = new HashMap<>();
	        map.put("code", 100);
	        map.put("msg", e.getMessage());
	        map.put("url", request.getRequestURL());
	        return map;
    }
	
	
	
	 
	
}
