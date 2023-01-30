package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录成功和失败的处理方法
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 实现使用BCrypt强哈希函数的密码编码器。客户机可以选择性地提供“强度”(即BCrypt中的日志轮数)和SecureRandom 实例。
     * 强度参数越大，需要做的工作就越多(指数级)来散列密码。默认值是10。
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth/login")
                // 登陆成功处理器
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        ObjectMapper om = new ObjectMapper();
                        String successMsg = om.writeValueAsString(om.writeValueAsString(authentication));
                        writer.write(successMsg);
                        writer.flush();
                        writer.close();
                    }
                })
                // 登陆失败处理器
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter writer = resp.getWriter();
                        writer.write(new ObjectMapper().writeValueAsString(e));
                        writer.flush();
                        writer.close();
                    }
                })
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest()
                .authenticated();

    }
}
