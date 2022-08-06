package com.kinnon.service;

import com.kinnon.domain.LoginTicket;
import com.kinnon.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Thinkpad
 * @description 针对表【user】的数据库操作Service
 * @createDate 2022-08-02 01:08:10
 */
public interface UserService extends IService<User> {

    public Map<String, Object> register(User user);

    public int activation(int userId, String code);

    public Map<String, Object> login(String username, String password, int expiredSenconds);

    public void logout(String ticket);

    public LoginTicket findLoginTicket(String ticket) ;



}
