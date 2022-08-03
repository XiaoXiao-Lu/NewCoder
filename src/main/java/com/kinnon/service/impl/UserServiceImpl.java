package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.User;
import com.kinnon.service.UserService;
import com.kinnon.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author Thinkpad
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-08-02 01:08:10
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




