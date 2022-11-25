package com.tulingxueyuan.mall.dto;

import com.tulingxueyuan.mall.modules.ums.model.UmsResource;
import com.tulingxueyuan.mall.modules.ums.model.UmsRole;
import lombok.Data;

import java.util.List;

/***
 * @Author 徐庶   QQ:1092002729
 * @Slogan 致敬大师，致敬未来的你
 */
@Data
public class ResourceRoleDTO extends UmsResource {

    private List<UmsRole> roleList;
}
