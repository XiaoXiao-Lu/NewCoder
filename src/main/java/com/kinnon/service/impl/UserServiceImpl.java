package com.kinnon.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.LoginTicket;
import com.kinnon.domain.User;
import com.kinnon.mapper.LoginTicketMapper;
import com.kinnon.service.UserService;
import com.kinnon.mapper.UserMapper;
import com.kinnon.util.MailClient;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.NewCoderUtil;
import com.kinnon.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Thinkpad
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-08-02 01:08:10
 */
@Service
@Transactional
public  class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService, NewCoderConstant {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${newcoder.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    public Map<String, Object> login(String username, String password, int expiredSenconds) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            map.put("usernameMsg", "用户名不存在");
            return map;
        }
        if(user.getStatus() == 0){
            map.put("usernameMsg", "用户未激活");
            return map;
        }
        String md5Password = NewCoderUtil.md5(password + user.getSalt());
        if (!md5Password.equals(user.getPassword())) {
            map.put("passwordMsg", "密码错误");
            return map;
        }
        //生成ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);
        loginTicket.setTicket(NewCoderUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSenconds * 1000));
//        loginTicketMapper.insertLoginTicket(loginTicket);


        String redisLoginTicketKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisLoginTicketKey,loginTicket);


        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(ticketKey,loginTicket);

    }


    @Override
    public Map<String, Object> register(User user) {
        HashMap<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("传入user非法!");
        }

        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("mailMsg", "邮箱不能为空");
            return map;
        }

        User user1 = userMapper.selectByUsername(user.getUsername());
        if (user1 != null) {
            map.put("usernameMsg", "用户名已存在");
            return map;
        }

        user1 = userMapper.selectByEmail(user.getEmail());
        if (user1 != null) {
            map.put("emailMsg", "邮箱已被注册");
            return map;
        }

        user.setSalt(NewCoderUtil.generateUUID().toString().substring(0, 5));
        user.setPassword(NewCoderUtil.md5(user.getPassword() + user.getSalt()));
        user.setStatus(0);
        user.setType(0);
        user.setActivationCode(NewCoderUtil.generateUUID());
        user.setCreateTime(new Date());
//        user.setHeaderUrl(String.format("http://images.newcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setHeaderUrl("https://api.uomg.com/api/rand.avatar?sort=%E7%94%B7&format=image");

        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);


//        templateEngine.set
        mailClient.sendMail(user.getEmail(), "激活牛客社区账号", content);

        return map;
    }

    /**
     * 激活账号
     *
     * @param userId
     * @param code
     * @return
     */
    @Override
    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateById(new User(user.getId(), 1));
            clearCache(userId);
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAIL;
        }


    }

    public LoginTicket findLoginTicket(String ticket) {

        return (LoginTicket)redisTemplate.opsForValue().get(RedisKeyUtil.getTicketKey(ticket));
    }

    @Override
    public int updateHeader(int userId, String headerUrl) {
        int i = userMapper.updateById(new User(userId, headerUrl));
        clearCache(userId);
        return i;
    }

    @Override
    public Map<String, Object> updatePassword(User user, String oldPassword, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        if (user != null) {
            String md5Password = NewCoderUtil.md5(oldPassword + user.getSalt());
            if (md5Password.equals(user.getPassword())) {
                user.setPassword(NewCoderUtil.md5(newPassword + user.getSalt()));
                userMapper.updateById(user);
                clearCache(user.getId());
            } else {
                map.put("oldPasswordMsg", "原密码错误");
            }
        } else {
            map.put("error", "用户不存在");
        }
        return map;

    }

    public User findUserByName(String name){
        return userMapper.selectByUsername(name);
    }

    //从缓存中取值
    public User getCache(int userId){
        String key = RedisKeyUtil.getUserKey(userId);
        return (User)redisTemplate.opsForValue().get(key);
    }

    public User inintCache(int userId){
        User user = userMapper.selectById(userId);
        String key = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(key,user,3600, TimeUnit.SECONDS);
        return user;
    }

    public void clearCache(int userId){
        String key = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(key);
    }

    @Override
    public User getById(Serializable id) {
        User user = getCache((int)id);
        if (user == null){
            user = inintCache((int)id);
        }
        return user;
    }
}




