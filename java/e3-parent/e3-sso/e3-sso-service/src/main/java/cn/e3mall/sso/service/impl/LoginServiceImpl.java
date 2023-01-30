package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * 用户登录处理
 * <p>Title: LoginServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p>
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    @Override
    public E3Result userLogin(String username, String password) {
        // 1、判断用户和密码是否正确
        //根据用户名查询用户信息
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        //执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if(list ==null || list.size()==0){
            //返回登录失败
            return E3Result.build(400,"用户名或者密码错误");

        }
        //取用用户信息
        TbUser user = list.get(0);
        //判断密码是否正确
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
        //如果不正确,返回登录失败
            E3Result.build(400,"用户名或者密码错误");

        }
        //正确生成token
        String token = UUID.randomUUID().toString();
        //把用户信息写入redis,key,token,value:用户信息
        user.setPassword(null);//把密码设置null,不传回页面
        jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
        //设置session 的过期时间
        jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
        //把token返回
        return E3Result.ok(token);
    }
}
