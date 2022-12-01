package com.tulingxueyuan.mall.modules.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tulingxueyuan.mall.modules.ums.mapper.UmsMemberMapper;
import com.tulingxueyuan.mall.modules.ums.service.UmsMemberService;
import com.tulingxueyuan.mall.modules.ums.model.UmsMember;
import org.springframework.stereotype.Service;
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

    @Override
    public UmsMember register(UmsMember umsMemberParam) {
        return null;
    }

    @Override
    public UmsMember login(String username, String password) {
        return null;
    }

    @Override
    public UmsMember getAdminByUsername(String username) {
        return null;
    }

    @Override
    public UmsMember getCurrentMember() {
        return null;
    }
}
