package com.mall.order.config;

import com.mall.order.domain.AdminUserDetails;
import com.mall.security.config.SecurityConfig;
import com.mall.security.config.component.dynamicSecurity.DynamicSecurityService;
import com.mall.sso.dto.ResourceRoleDTO;
import com.mall.sso.model.UmsAdmin;
import com.mall.sso.model.UmsRole;
import com.mall.sso.service.UmsAdminService;
import com.mall.sso.service.UmsResourceService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 *
 * 1.查询出资源对应的角色
 * 2.只需要后台服务才配置动态权限
 */
@Configuration
//@EnableWebSecurity  // 启动
public class MallSecurityConfig extends SecurityConfig  {

    @Reference
    private UmsAdminService umsAdminService;

    @Reference
    private UmsResourceService umsResourceService;

    /**
     * 认证交给springsecurity
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return new UserDetailsService(){

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UmsAdmin umsAdmin = umsAdminService.loadUserByUsername(username);
                if (umsAdmin != null) {
                    // 查询用户访问资源，暂留， 后续改动
                    List<UmsRole> resourceList = umsAdminService.loadUserById(umsAdmin.getId());
                    AdminUserDetails adminUserDetails = new AdminUserDetails(umsAdmin, resourceList);
                    return adminUserDetails;
                }
                return null;
            }
        };
    }

    // 获取最新的资源角色信息
    @Bean("dynamicSecurityService")
    public DynamicSecurityService dynamicSecurityService() {
        return  new DynamicSecurityService() {
            @Reference
            private UmsResourceService umsResourceService;
            @Override
            public Map<RequestMatcher, List<ConfigAttribute>> loadDataSource() {
                Map<RequestMatcher, List<ConfigAttribute>> map = new ConcurrentHashMap<>();
                List<ResourceRoleDTO> list= umsResourceService.getAllResourceRole();
                for (ResourceRoleDTO resource : list) {
                    // 通配符匹配器
                    map.put(new AntPathRequestMatcher(resource.getUrl()),
                            // 所有角色信息
                            resource.getRoleList().stream()
                                    .map(role-> new org.springframework.security.access.SecurityConfig(role.getName()))
                                    .collect(Collectors.toList())
                    );
                }
                return map;
            }
        };
    }
}
