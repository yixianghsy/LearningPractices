package com.mall.sso.service.impl;

import com.mall.sso.mapper.UmsAdminRoleRelationDao;
import com.mall.sso.mapper.UmsAdminRoleRelationMapper;
import com.mall.sso.model.UmsAdminRoleRelation;
import com.mall.sso.model.UmsAdminRoleRelationExample;
import com.mall.sso.service.UmsAdminRoleRelationService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class UmsAdminRoleRelationServiceImpl implements UmsAdminRoleRelationService
{
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;

    @Override
    public List<UmsAdminRoleRelation> list(UmsAdminRoleRelationExample example) {
        return adminRoleRelationMapper.selectByExample(example);
    }
}
