package com.mengxuegu.security;

import com.mengxuegu.security.authentication.mobile.SmsSend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
//@Component
public class MobileSmsCodeSender implements SmsSend {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean sendSms(String mobile, String content) {
        logger.info("Web应用新的短信验证码接口---向手机号"+mobile+"发送了验证码为：" + content);
        return false;
    }
}
