package com.mall.portal.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "trade.zhifu.qrcode")
public class TradePayProp {
    private String  paySuccessCallBack;
    private String storePath;
    private String httpBasePath;
}
