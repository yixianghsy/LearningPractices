package com.mall.portal.config;


import com.mall.portal.component.TradePayProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限验证拦截器
 */
@Configuration
public class GlobalWebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    TradePayProp tradePayProp;




    // 将物理文件夹中的支付二维码映射为静态资源路径
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(tradePayProp.getHttpBasePath()+"/**")
                .addResourceLocations("file:"+tradePayProp.getStorePath()+"/");
    }
}
