package com.kinnon.mapper;

import com.kinnon.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【message】的数据库操作Mapper
* @createDate 2022-08-02 14:42:19
* @Entity com.kinnon.domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    /**
     * 查询当前用户的会话列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectConversations(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    int selectConversationCount(int userId);


    /**
     * 查询当前用户当前会话收到的私信消息
     * @param
     * @return
     */
    List<Message> selectLetters(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询当前会话私信的数量
     * @param conversationId
     * @return
     */
    int selectLetterCount(String conversationId);

    /**
     * 查询当前用户的未读私信数量
     * @param userId
     * @return
     */
    int selectLetterUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    public int insertMessage(Message message);

    public int updateMessageStatus(@Param("ids") List<Integer> ids, @Param("status") int status);

    //查询某个主题下的最新消息
    Message selectLatestNotice(@Param("userId") int userId, @Param("topic") String topic);
    //查询某个主题下的所有消息数量
    int selectNoticeCount(@Param("userId") int userId, @Param("topic") String topic);
    //查询某个主题未读消息数量
    int selectNoticeUnreadCount(@Param("userId") int userId, @Param("topic") String topic);

    //查询某个主题下的通知列表
    List<Message> selectNotices(@Param("userId") int userId, @Param("topic") String topic, @Param("offset") int offset, @Param("limit") int limit);

}




