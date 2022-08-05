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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Thinkpad
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-08-02 01:08:10
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService, NewCoderConstant {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


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
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateLoginTicket(ticket, 1);
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
        user.setHeaderUrl(String.format("http://images.newcoder.com/head/%dt.png", new Random().nextInt(1000)));

        userMapper.insertUser(user);

        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);


//        templateEngine.set
        mailClient.sendMail(user.getEmail(), "激活牛客社区账号", content);

        return null;
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
            return ACTIVATION_SUCCESS;
        } else {
            return ACTIVATION_FAIL;
        }


    }


}




