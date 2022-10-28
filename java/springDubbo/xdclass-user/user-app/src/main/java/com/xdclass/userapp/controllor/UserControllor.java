package com.xdclass.userapp.controllor;

import com.xdclass.userapi.VO.UserVO;
import com.xdclass.userapp.service.dubbo.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author daniel
 */
@RestController
public class UserControllor {


    @Resource
    private UserService userService;


    /**
     * 通过用户id获取用户信息接口
     * @param id
     * @return
     */
    @RequestMapping("/getUserById")
    public UserVO getUserById(Integer id){
        if(id==null||id==0){
            return new UserVO();
        }
        return userService.getUserById(id);
    }

}
