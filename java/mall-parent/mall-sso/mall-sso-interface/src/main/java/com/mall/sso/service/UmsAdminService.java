package com.mall.sso.service;

import com.mall.api.CommonPage;
import com.mall.sso.dto.UmsAdminParam;
import com.mall.sso.dto.UpdateAdminPasswordParam;
import com.mall.sso.model.UmsAdmin;
import com.mall.sso.model.UmsResource;
import com.mall.sso.model.UmsRole;

import java.util.List;

/**
 * 后台用户管理Service
 * Created by macro on 2018/4/26.
 */
public interface UmsAdminService {
    /**
     * 根据用户名获取后台管理员
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 注册功能
     */
    UmsAdmin register(UmsAdminParam umsAdminParam);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    UmsAdmin login(String username, String password);



    /**
     * 根据用户名或昵称分页查询用户
     */
    CommonPage list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     */
    boolean update(Long id, UmsAdmin admin);

    /**
     * 删除指定用户
     */
    boolean delete(Long id);

    /**
     * 修改用户角色关系
     */

    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对于角色
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 获取指定用户的可访问资源
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 修改密码
     */
    int updatePassword(UpdateAdminPasswordParam updatePasswordParam);

    /**
     * 获取用户信息
     * 使用泛型标注返回类型。
     */
//    <T> T loadUserByUsername(String username);
    public UmsAdmin loadUserByUsername(String username);
    public List<UmsRole> loadUserById(Long id);
    String refreshToken(String token);

    UmsAdmin getById(Long adminId);
}
