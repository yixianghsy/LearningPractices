package com.tulingxueyuan.mall.config;

import com.tulingxueyuan.mall.modules.ums.service.UmsMemberService;
import com.tulingxueyuan.mall.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Configuration
@EnableWebSecurity  // 启动
public class MallSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsMemberService memberService;

    /**
     * 认证交给springsecurity
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> memberService.loadUserByUsername(username);
    }

}
