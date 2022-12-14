package com.tulingxueyuan.mall.domain;

import com.tulingxueyuan.mall.modules.ums.model.UmsAdmin;
import com.tulingxueyuan.mall.modules.ums.model.UmsRole;
import com.tulingxueyuan.mall.modules.ums.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public class AdminUserDetails implements UserDetails {

    // 用户信息
    UmsAdmin umsAdmin;

    List<UmsRole> roleList;
     

    public AdminUserDetails(UmsAdmin umsAdmin, List<UmsRole> roleList) {
        this.umsAdmin = umsAdmin;
        this.roleList = roleList;
    }

    public UmsAdmin getUmsAdmin() {
        return umsAdmin;
    }


    /**
     * 返回当前用户的角色信息
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //List<UmsRole> roleList = adminService.getRoleList(umsAdmin.getId());
        return  roleList.stream()
                .map(role->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return umsAdmin.getPassword();
    }

    @Override
    public String getUsername() {
        return umsAdmin.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return umsAdmin.getStatus()==1;
    }
}
