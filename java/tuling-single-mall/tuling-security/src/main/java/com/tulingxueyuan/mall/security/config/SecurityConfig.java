package com.tulingxueyuan.mall.security.config;

import com.tulingxueyuan.mall.common.util.JwtTokenUtil;
import com.tulingxueyuan.mall.security.config.component.*;
import com.tulingxueyuan.mall.security.config.component.dynamicSecurity.DynamicAccessDecisionManager;
import com.tulingxueyuan.mall.security.config.component.dynamicSecurity.DynamicSecurityFilter;
import com.tulingxueyuan.mall.security.config.component.dynamicSecurity.DynamicSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Map;


/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 由于前台服务没有动态权限功能，所以要配置required = false
     */
    @Autowired(required = false)
    SecurityResourceRoleSource securityResourceRoleSource;

    @Autowired(required = false)
    private DynamicSecurityMetadataSource dynamicSecurityService;

    /**
     * 权限配置   白名单...jwt认证
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();

        // 循环白名单进行放行
        for (String url : ignoredUrlsConfig().getUrls()) {
            registry.antMatchers(url).permitAll();
        }

       /* 静态资源权限*/
       if(securityResourceRoleSource!=null) {
            Map<String, List<String>> resourceRole = securityResourceRoleSource.getResourceRole();

            //循环注册registry.antMatchers("/product").hasAnyAuthority("xxx管理员")
            for (String resource : resourceRole.keySet()) {

                // 将List转换数组， 将object[] 转换string[]
                List<String> roles = resourceRole.get(resource);
                registry.antMatchers(resource).hasAnyAuthority(roles.toArray(new String[roles.size()]));
            }
        }

        // 允许可以请求OPTIONS CORS
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();

        // 其他任何请求都需要身份认证
        registry
                // 任何请求
                .anyRequest()
                // 都需要认证
                .authenticated()
                    // 关闭csrf跨站请求伪造 :因为现在使用jwt来实现认证
            .and()
                // 支持跨域
                .cors()
            .and()
                .csrf()
            .disable()
                    // 禁止session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // 自定义权限拒绝处理类
            .and()
                .exceptionHandling()
                // 没有权限访问时的处理类
                .accessDeniedHandler(restfulAccessDeniedHandler())
                // 没有登录时的处理类
                .authenticationEntryPoint(restfulAuthenticationEntryPoint())
            .and()
                    // 加入jwt认证过滤器
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            //有动态权限配置时添加动态权限校验过滤器
             if(dynamicSecurityService!=null){
                registry.and().addFilterBefore(dynamicSecurityFilter(), FilterSecurityInterceptor.class);
            }
    }
    /**
     * jwt工具类
     * @return
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IgnoredUrlsConfig ignoredUrlsConfig(){
        return new IgnoredUrlsConfig();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler(){
        return new RestfulAccessDeniedHandler();
    }


    @Bean
    public RestfulAuthenticationEntryPoint restfulAuthenticationEntryPoint(){
        return new RestfulAuthenticationEntryPoint();
    }

    /**
     * 作用：根据当前请求url获取对应角色
     * @return
     */
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }

    /**
     * 作用：在FilterSecurityInterceptor前面的自定义过滤器
     * @return
     */
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityFilter dynamicSecurityFilter() {
        return new DynamicSecurityFilter();
    }

    /**
     * 作用：鉴权
     * @return
     */
    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource();
    }


}
