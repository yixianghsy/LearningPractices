package com.mengxuegu.security.config;

import com.mengxuegu.security.authentication.code.ImageCodeValidateFilter;
import com.mengxuegu.security.authentication.mobile.MobileAuthenticationConfig;
import com.mengxuegu.security.authentication.mobile.MobileValidateFilter;
import com.mengxuegu.security.authentication.session.CustomLogoutHandler;
import com.mengxuegu.security.authorize.AuthorizeConfigurerManager;
import com.mengxuegu.security.properites.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;


/**
 * alt+/ 导包
 * ctrl+o 覆盖
 * @Auther: 梦学谷 www.mengxuegu.com
 */
@Configuration
@EnableWebSecurity // 开启springsecurity过滤链 filter
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启注解方法级别权限控制
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    public PasswordEncoder passwordEncoder() {
        // 明文+随机盐值》加密存储
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsService customUserDetailsService;

    /**
     * 认证管理器：
     * 1. 认证信息（用户名，密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 数据库存储的密码必须是加密后的，不然会报错：There is no PasswordEncoder mapped for the id "null"
//        String password = passwordEncoder().encode("1234");
//        logger.info("加密之后存储的密码：" + password);
//        auth.inMemoryAuthentication().withUser("mengxuegu")
//                .password(password).authorities("ADMIN");
        auth.userDetailsService(customUserDetailsService);
    }



    // 配置文件参数
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;

    @Autowired
    DataSource dataSource;

    // 校验手机验证码
    @Autowired
    private MobileValidateFilter mobileValidateFilter;

    // 校验手机号是否存在，就是手机号认证
    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    /**
     * 当同个用户session数量超过指定值之后 ,会调用这个实现类
     */
    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;


    @Autowired
    private AuthorizeConfigurerManager authorizeConfigurerManager;
    /**
     * 记住我功能
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 是否启动项目时自动创建表，true自动创建
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }
    /**
     * 当你认证成功之后 ，springsecurity它会重写向到你上一次请求上
     * 资源权限配置：
     * 1. 被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic() // 采用 httpBasic认证方式
            // 校验手机验证码过滤器
            http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin() // 表单登录方式
                    .loginPage(securityProperties.getAuthentication().getLoginPage())
                    .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) // 登录表单提交处理url, 默认是/login
                    .usernameParameter(securityProperties.getAuthentication().getUsernameParameter()) //默认的是 username
                    .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())  // 默认的是 password
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
//                .and()
//                    .authorizeRequests() // 授权请求
//                    .antMatchers(securityProperties.getAuthentication().getLoginPage(),
////                    "/code/image","/mobile/page", "/code/mobile"
//                            securityProperties.getAuthentication().getImageCodeUrl(),
//                            securityProperties.getAuthentication().getMobilePage(),
//                            securityProperties.getAuthentication().getMobileCodeUrl()
//                    ).permitAll() // 放行/login/page不需要认证可访问
//
//                    // 有 sys:user 权限的可以访问任意请求方式的/role
//                    .antMatchers("/user").hasAuthority("sys:user")
//                    // 有 sys:role 权限的可以访问 get方式的/role
//                    .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
//                    .antMatchers(HttpMethod.GET, "/permission")
//                    // ADMIN 注意角色会在前面加上前缀 ROLE_ , 也就是完整的是 ROLE_ADMIN, ROLE_ROOT
//                    .access("hasAuthority('sys:premission') or hasAnyRole('ADMIN', 'ROOT')")
//
//                    .anyRequest().authenticated() //所有访问该应用的http请求都要通过身份认证才可以访问
                .and()
                    .rememberMe() // 记住功能配置
                    .tokenRepository(jdbcTokenRepository()) //保存登录信息
                    .tokenValiditySeconds(securityProperties.getAuthentication().getTokenValiditySeconds()) //记住我有效时长
                .and()
                    .sessionManagement()// session管理
                    .invalidSessionStrategy(invalidSessionStrategy) //当session失效后的处理类
                    .maximumSessions(1) // 每个用户在系统中最多可以有多少个session
                    .expiredSessionStrategy(sessionInformationExpiredStrategy)// 当用户达到最大session数后，则调用此处的实现
                    .maxSessionsPreventsLogin(true) // 当一个用户达到最大session数,则不允许后面再登录
                    .sessionRegistry(sessionRegistry())
                .and().and()
                    .logout()
                    .addLogoutHandler(customLogoutHandler) // 退出清除缓存
                    .logoutUrl("/user/logout") // 退出请求路径/；意思是页面登录退出的地址？？？
                    .logoutSuccessUrl("/mobile/page") //退出成功后跳转地址
                    .deleteCookies("JSESSIONID") // 退出后删除什么cookie值
            ;// 注意不要少了分号

        http.csrf().disable(); // 关闭跨站请求伪造
        //将手机认证添加到过滤器链上
        http.apply(mobileAuthenticationConfig);

        // 将所有的授权配置统一的起来
        authorizeConfigurerManager.configure(http.authorizeRequests());
    }

    /**
     * 退出清除缓存
     */
    @Autowired
    private CustomLogoutHandler customLogoutHandler;
    /**
     * 为了解决退出重新登录问题
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    /**
     * 一般是针对静态资源放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
    }
}
