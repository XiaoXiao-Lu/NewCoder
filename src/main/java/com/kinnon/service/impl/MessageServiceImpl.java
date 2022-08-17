package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.Message;
import com.kinnon.service.MessageService;
import com.kinnon.mapper.MessageMapper;
import com.kinnon.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【message】的数据库操作Service实现
* @createDate 2022-08-02 14:42:19
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> selectConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    public int getLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public List<Message> selectLetters( String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int getConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    public int getLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    public int addMessage(Message message) {
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readStatus(List<Integer> ids, int status) {
        return messageMapper.updateMessageStatus(ids, status);
    }



    public int updateMessageStatus(List<Integer> ids, int status) {
        return messageMapper.updateMessageStatus(ids, status);

    }

    public Message getLatestMessage(int userId,String topic) {
        return  messageMapper.selectLatestNotice(userId,topic);
    }

    public int getNoticeCount(int userId,String topic) {
        return messageMapper.selectNoticeCount(userId,topic);
    }

    public int getNoticeUnreadCount(int userId,String topic) {
        return messageMapper.selectNoticeUnreadCount(userId,topic);
    }

    public List<Message> getNotices(int userId,String topic,int offset,int limit) {
        return messageMapper.selectNotices(userId,topic,offset,limit);
    }




}




