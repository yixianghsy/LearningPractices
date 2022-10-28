package com.xdclass.userapp.service.dubbo;

import com.xdclass.userapi.VO.UserVO;
import com.xdclass.userapi.service.IUserService;
import com.xdclass.userapp.domain.TUser;
import com.xdclass.userapp.mapper.TUserMapper;
import org.springframework.beans.BeanUtils;
import org.apache.dubbo.config.annotation.Service;
import javax.annotation.Resource;

/**
 * @author daniel
 */
@Service
public class UserService implements IUserService {

    @Resource
    private TUserMapper userMapper;


    /**
     * 通过用户id获取用户信息接口
     * @param id
     * @return
     */
    @Override
    public UserVO getUserById(int id){
        TUser tUser = userMapper.selectByPrimaryKey(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(tUser,userVO);
        return userVO;
    }


}
