package com.mall.sso.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.mall.api.CommonResult;
import com.mall.sso.mapper.UmsMemberMapper;
import com.mall.sso.model.UmsAdmin;
import com.mall.sso.model.UmsMember;
import com.mall.sso.model.UmsMemberExample;
import com.mall.sso.service.UmsMemberService;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class UmsMemberServiceImpl implements UmsMemberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UmsMemberMapper memberMapper;
    @Override
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }
        return null;
    }

    @Override
    public UmsMember getById(Long id) {
        return null;
    }

    @Override
    public UmsMember register(UmsMember umsAdminParam) {
        UmsMember umsMember = new UmsMember();
        BeanUtils.copyProperties(umsAdminParam, umsMember);
        umsMember.setCreateTime(new Date());
        umsMember.setStatus(1);
        //查询是否已有该用户
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(umsMember.getUsername());
        List<UmsMember> umsMembers = memberMapper.selectByExample(example);
        if (umsMembers.size() > 0) {
            return null;
        }
        //将密码进行加密操作
        String encodePassword = BCrypt.hashpw(umsMember.getPassword());
        umsMember.setPassword(encodePassword);
        memberMapper.insert(umsMember);
        return umsMember;

    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        return null;
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        return null;
    }

    @Override
    public UmsMember getCurrentMember() {
        return null;
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {

    }

    @Override
    public UmsMember loadUserByUsername(String username) {
        UmsMember member = getByUsername(username);
        return  member;
    }

    @Override
    public UmsMember login(String username, String password) {
        UmsMember umsMember=null;
        //密码需要客户端加密后传递
        try {
            umsMember = loadUserByUsername(username);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return umsMember;
    }

    @Override
    public String refreshToken(String token) {
        return null;
    }

    @Override
    public int updateUmsMember(UmsMember umsMember) {
        return 0;
    }
}
