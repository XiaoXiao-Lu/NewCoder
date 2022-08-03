package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.LoginTicket;
import com.kinnon.service.LoginTicketService;
import com.kinnon.mapper.LoginTicketMapper;
import org.springframework.stereotype.Service;

/**
* @author Thinkpad
* @description 针对表【login_ticket】的数据库操作Service实现
* @createDate 2022-08-02 14:42:16
*/
@Service
public class LoginTicketServiceImpl extends ServiceImpl<LoginTicketMapper, LoginTicket>
    implements LoginTicketService{

}




