package com.kinnon;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.kinnon.domain.LoginTicket;
import com.kinnon.domain.User;
import com.kinnon.mapper.DiscussPostMapper;
import com.kinnon.mapper.LoginTicketMapper;
import com.kinnon.mapper.UserMapper;
import com.kinnon.service.DiscussPostService;
import com.kinnon.service.UserService;
import com.kinnon.service.impl.DiscussPostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpringbootProjectDemo1ApplicationTests {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    void contextLoads() {
//        System.out.println(discussPostService.getDiscussPostRows(0));
        System.out.println(discussPostService.getDisCussPosts(0, 0, 10));
    }
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void test1 (){
        System.out.println(discussPostMapper.selectDisCussPosts(0, 0, 10));
    }

    @Test
    void test2 (){
        System.out.println(discussPostMapper.selectDiscussPostRows(0));
    }


    @Autowired
    private UserMapper userMapper;
    @Test
    public void test3(){
        User user = new User();
        user.setUsername("kin22non");
        user.setPassword("9d999");
        user.setStatus(0);
        int insert = userMapper.insertUser(user);
        System.out.println(user.getId());
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void test4(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(3);
        loginTicket.setTicket("1232");
        loginTicket.setStatus(0);
        int insert = loginTicketMapper.insert(loginTicket);

    }

}
