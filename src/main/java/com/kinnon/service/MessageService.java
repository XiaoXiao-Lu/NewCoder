package com.kinnon.service;

import com.kinnon.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【message】的数据库操作Service
* @createDate 2022-08-02 14:42:19
*/
public interface MessageService extends IService<Message> {

    public List<Message> selectConversations(int userId, int offset, int limit) ;

    public int getLetterUnreadCount(int userId, String conversationId) ;

    public List<Message> selectLetters( String conversationId, int offset, int limit) ;

    public int getConversationCount(int userId) ;

    public int getLetterCount(String conversationId) ;

    public int addMessage(Message message);



    public int updateMessageStatus(List<Integer> ids, int status) ;




}
