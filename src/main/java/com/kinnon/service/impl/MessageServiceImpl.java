package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.Message;
import com.kinnon.service.MessageService;
import com.kinnon.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Thinkpad
* @description 针对表【message】的数据库操作Service实现
* @createDate 2022-08-02 14:42:19
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




