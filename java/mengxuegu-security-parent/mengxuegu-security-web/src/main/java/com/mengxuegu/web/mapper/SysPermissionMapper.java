package com.mengxuegu.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengxuegu.web.entities.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: 梦学谷 www.mengxuegu.com
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {


    List<SysPermission> selectPermissionByUserId(@Param("userId") Long userId);

    List<SysPermission> findByRoleId(@Param("roleId") Long roleId);


}
