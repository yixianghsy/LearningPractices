package com.mall.portal.config;


import com.mall.portal.domain.MemberDetails;
import com.mall.security.config.SecurityConfig;
import com.mall.sso.model.UmsMember;
import com.mall.sso.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Reference;
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

    @Reference
    private UmsMemberService memberService;

    /**
     * 认证交给springsecurity
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserDetails userDetails = new MemberDetails(memberService.loadUserByUsername(username));
                return userDetails;
            }
        };
    }

}
