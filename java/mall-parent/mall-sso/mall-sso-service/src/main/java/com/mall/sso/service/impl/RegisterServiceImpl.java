package com.mall.sso.service.impl;
import com.mall.mapper.TbUserMapper;
import com.mall.mapper.pojo.TbUser;
import com.mall.mapper.pojo.TbUserExample;
import com.mall.sso.service.RegisterService;
import com.mall.utils.E3Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 用户注册处理Service
 * <p>Title: RegisterServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class RegisterServiceImpl  implements RegisterService {
    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkData(String param, int type) {
        //根据不同的type生成不同的查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1：用户名 2：手机号 3：邮箱
        if(type==1){
            criteria.andUsernameEqualTo(param);
        }else if (type ==2){
            criteria.andPhoneEqualTo(param);
        }else if(type ==3){
            criteria.andEmailEqualTo(param);
        }else{
            return E3Result.build(400,"数据类型错误");

        }
        //执行查询
        List<TbUser> list = tbUserMapper.selectByExample(example);
        //判断结果中是否包含数据
        if(list!=null&&list.size()>0){
            //有数据返回false
            return E3Result.ok(false);
        }
        //没有数据返回true
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser user) {
        //数据有效性校验
        if (StringUtils.isBlank(user.getUsername())|| StringUtils.isBlank(user.getPassword())|| StringUtils.isBlank(user.getPhone())){
        return  E3Result.build(400,"用户数据不完整,注册失败");
        }
        //1：用户名 2：手机号 3：邮箱
        //1：用户名 2：手机号 3：邮箱
        E3Result result = checkData(user.getUsername(), 1);
        if (!(boolean) result.getData()) {
            return E3Result.build(400, "此用户名已经被占用");
        }
        result = checkData(user.getPhone(), 2);
        if (!(boolean)result.getData()) {
            return E3Result.build(400, "手机号已经被占用");
        }
        //补全pojo的属性
        user.setCreated(new Date());
        user.setUpdated(new Date());
        //对密码进行md5加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5DigestAsHex);
        //把用户数据插入到数据库中
        tbUserMapper.insert(user);
        //返回添加成功
        return E3Result.ok();
    }
}
