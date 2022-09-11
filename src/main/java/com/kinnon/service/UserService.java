package com.kinnon.service;

import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.LoginTicket;
import com.kinnon.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
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

    public LoginTicket findLoginTicket(String ticket);

    public int updateHeader(int userId, String headerUrl);

    public Map<String, Object> updatePassword(User user, String oldPassword, String newPassword);

    public User findUserByName(String name);

    public Collection<? extends GrantedAuthority> getAuthorities(int userId);

}




