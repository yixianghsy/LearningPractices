package com.mengxuegu.security;

import com.mengxuegu.web.entities.SysPermission;
import com.mengxuegu.web.entities.SysUser;
import com.mengxuegu.web.service.SysPermissionService;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
public abstract class AbstractUserDetailsService implements UserDetailsService {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 这个方法交给子类去实现它，查询用户信息
     * @param usernameOrMobile 用户名或者手机号
     * @return
     */
    public abstract SysUser findSysUser(String usernameOrMobile);

    @Override
    public UserDetails loadUserByUsername(String usernameOrMobile) throws UsernameNotFoundException {
        // 1. 通过请求的用户名去数据库中查询用户信息
        SysUser sysUser = findSysUser(usernameOrMobile);
        // 通过用户id去获取权限信息
        findSysPermission(sysUser);

        return sysUser;
    }

        private void findSysPermission(SysUser sysUser) {
        if(sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 2. 查询该用户有哪一些权限
        List<SysPermission> permissions = sysPermissionService.findByUserId(sysUser.getId());

        if(CollectionUtils.isEmpty(permissions)) {
            return ;
        }

        // 在左侧菜单 动态渲染会使用，目前先把它都传入
        sysUser.setPermissions(permissions);

        // 3. 封装权限信息
        List<GrantedAuthority> authorities = Lists.newArrayList();
        for(SysPermission sp: permissions) {
            // 权限标识
            String code = sp.getCode();
            authorities.add(new SimpleGrantedAuthority(code));
        }
        sysUser.setAuthorities(authorities);
    }
}
