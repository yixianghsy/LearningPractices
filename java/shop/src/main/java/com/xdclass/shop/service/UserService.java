/**
 *
 */
package com.xdclass.shop.service;

import com.xdclass.shop.model.User;
import com.xdclass.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Daniel
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userDao;

    public boolean checkLogin(User user) {
        user = userDao.findByUsernameAndPassword(user.getUsername(),
                user.getPassword());
        return user != null;
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userDao.findByUsernameAndPassword(username, password);
        return user;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User findOne(Integer id) {
        return userDao.findOne(id);
    }

}
