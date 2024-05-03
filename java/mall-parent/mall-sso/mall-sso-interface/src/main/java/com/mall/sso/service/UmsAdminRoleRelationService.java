package com.mall.sso.service;

import com.mall.sso.model.UmsAdminRoleRelation;
import com.mall.sso.model.UmsAdminRoleRelationExample;

import java.util.List;

/**
 * 管理员角色关系管理Service
 * Created by macro on 2020/8/21.
 */
public interface UmsAdminRoleRelationService {
    List<UmsAdminRoleRelation> list(UmsAdminRoleRelationExample example );
}
