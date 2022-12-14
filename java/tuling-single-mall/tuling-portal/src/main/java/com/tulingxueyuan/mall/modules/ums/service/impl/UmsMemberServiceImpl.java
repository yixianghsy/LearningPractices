package com.tulingxueyuan.mall.modules.ums.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tulingxueyuan.mall.common.exception.ApiException;
import com.tulingxueyuan.mall.common.exception.Asserts;
import com.tulingxueyuan.mall.domain.MemberDetails;
import com.tulingxueyuan.mall.modules.ums.mapper.UmsMemberLoginLogMapper;
import com.tulingxueyuan.mall.modules.ums.mapper.UmsMemberMapper;
import com.tulingxueyuan.mall.modules.ums.model.UmsMemberLoginLog;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberCacheService;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberService;
import com.tulingxueyuan.mall.modules.ums.model.UmsMember;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author XuShu
 * @since 2021-03-19
 */
@Service
public class UmsMemberServiceImpl extends ServiceImpl<UmsMemberMapper, UmsMember> implements UmsMemberService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UmsMemberCacheService memberCacheService;

    @Autowired
    UmsMemberLoginLogMapper loginLogMapper;

    @Override
    public UmsMember register(UmsMember umsAdminParam) {
        UmsMember umsMember = new UmsMember();
        BeanUtils.copyProperties(umsAdminParam, umsMember);
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        //查询是否有相同用户名的用户
        QueryWrapper<UmsMember> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMember::getUsername,umsMember.getUsername());
        List<UmsMember> umsAdminList = list(wrapper);
        if (umsAdminList.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = BCrypt.hashpw(umsMember.getPassword());
        umsMember.setPassword(encodePassword);
        baseMapper.insert(umsMember);
        return umsMember;
    }

    @Override
    public UmsMember login(String username, String password) {

        //密码需要客户端加密后传递
        UmsMember umsAdmin=null;
        try {
            UserDetails userDetails =  loadUserByUsername(username);
            umsAdmin=((MemberDetails)userDetails).getUmsMember();

            if(!passwordEncoder.matches(password,umsAdmin.getPassword())){
//            if(!passwordEncoder.matches("123456","$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG")){
                Asserts.fail("密码不正确");
            }

            // 生成springsecurity的通过认证标识
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            if(!userDetails.isEnabled()){
                Asserts.fail("帐号已被禁用");
            }
            insertLoginLog(username);
        } catch (Exception e) {
            Asserts.fail("登录异常:"+e.getMessage());
        }
        return umsAdmin;
    }

    @Override
    public UmsMember getAdminByUsername(String username) {
        //先去redis里获取
        UmsMember user = memberCacheService.getUser(username);
        if(user!=null) return  user;
        QueryWrapper<UmsMember> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMember::getUsername,username);
        List<UmsMember> adminList = list(wrapper);
        if (adminList !=null &&  adminList.size() >0){
            user = adminList.get(0);
            memberCacheService.setUser(user);
            return user;

        }
        return null;
    }
    /**
     * 获得当前用户
     * @return
     */
    @Override
    public UmsMember getCurrentMember() {
        //标识
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails memberDetails =(MemberDetails) authentication.getPrincipal();
        return memberDetails.getUmsMember();
    }
    /**
     * 添加登录记录
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsMember user = getAdminByUsername(username);
        if(user==null) return;
        UmsMemberLoginLog loginLog = new UmsMemberLoginLog();
        loginLog.setMemberId(user.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLogMapper.insert(loginLog);
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsMember umsMember = getAdminByUsername(username);
        if(umsMember!=null){
            return new MemberDetails(umsMember);
        }
        throw  new ApiException("用户名或密码错误!");
    }
}
