package com.tulingxueyuan.mall.security.config.component;

import java.util.List;
import java.util.Map;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
public interface SecurityResourceRoleSource {
    /**
     * 获取所有资源对应的橘色
     // key: 资源： /product/**
     // value: 角色
     * @return
     */
    Map<String, List<String>> getResourceRole();
}
