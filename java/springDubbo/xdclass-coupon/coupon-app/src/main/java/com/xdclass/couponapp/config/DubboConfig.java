package com.xdclass.couponapp.config;

import org.apache.dubbo.config.MetadataReportConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DubboConfig {

    @Bean
    public MetadataReportConfig metadataReportConfig() {
        MetadataReportConfig metadataReportConfig = new MetadataReportConfig();
        metadataReportConfig.setAddress("zookeeper://120.24.80.45:2181");
        return metadataReportConfig;
    }

}
