package com.tulingxueyuan.mall.config;

import cn.hutool.core.util.ArrayUtil;
import com.tulingxueyuan.mall.dto.ResourceRoleDTO;
import com.tulingxueyuan.mall.modules.ums.model.UmsRole;
import com.tulingxueyuan.mall.modules.ums.service.UmsAdminService;
import com.tulingxueyuan.mall.modules.ums.service.UmsResourceService;
import com.tulingxueyuan.mall.security.config.SecurityConfig;
import com.tulingxueyuan.mall.security.config.component.SecurityResourceRoleSource;
import com.tulingxueyuan.mall.security.config.component.dynamicSecurity.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.HashMap;
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
@EnableWebSecurity  // 启动
public class MallSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsAdminService umsAdminService;

    @Autowired
    private UmsResourceService umsResourceService;

    /**
     * 认证交给springsecurity
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> umsAdminService.loadUserByUsername(username);
    }

    /**
     * 为springsecurity配置的资源角色信息（静态）
     * @return
     */
    //@Bean
    public SecurityResourceRoleSource securityResourceRoleSource(){
        return () -> {
            // Map<String, List<String>>
            // 调用业务逻辑类查询资源对应角色信息
            List<ResourceRoleDTO> list= umsResourceService.getAllResourceRole();
            Map<String, List<String>> map=new HashMap<>();
            for (ResourceRoleDTO resourceRoleDTO : list) {
                List<String> roleNamelist= resourceRoleDTO.getRoleList()
                        .stream()
                        .map(role-> role.getName())
                        .collect(Collectors.toList());

                map.put(resourceRoleDTO.getUrl(),roleNamelist);
            }
            return map;
        };
    }

    // 获取最新的资源角色信息
    @Bean("dynamicSecurityService")
    public DynamicSecurityService dynamicSecurityService(UmsResourceService umsResourceService) {
        return () -> {
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
        };
    }

}
