package com.tulingxueyuan.mall.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
@Component
@ConfigurationProperties(prefix = "trade.zhifu.qrcode")
public class TradePayProp {

    private String  paySuccessCallBack;
    private String storePath;
    private String httpBasePath;
}
