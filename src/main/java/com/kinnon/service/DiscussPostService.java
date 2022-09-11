package com.kinnon.service;

import com.kinnon.domain.DiscussPost;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【discuss_post】的数据库操作Service
* @createDate 2022-08-02 14:42:03
*/
public interface DiscussPostService extends IService<DiscussPost> {

    List<DiscussPost> getDisCussPosts(int userId,  int offset,  int limit);

    int getDiscussPostRows( int userId);

    public int addDiscussPost(DiscussPost discussPost);

    public int updateCommentCount(int id, int commentCount);

    public int selectDiscussPostCount(int userId) ;

    public int updateStatus(int id,int status);

    public int updateType(int id,int type);

}
