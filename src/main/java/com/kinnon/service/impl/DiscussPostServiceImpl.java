package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.Comment;
import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.User;
import com.kinnon.service.DiscussPostService;
import com.kinnon.mapper.DiscussPostMapper;
import com.kinnon.service.UserService;
import com.kinnon.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【discuss_post】的数据库操作Service实现
* @createDate 2022-08-02 14:42:03
*/
@Service
public class DiscussPostServiceImpl extends ServiceImpl<DiscussPostMapper, DiscussPost>
    implements DiscussPostService{

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    @Override
    public List<DiscussPost> getDisCussPosts(int userId, int offset, int limit) {
        List<DiscussPost> discussPosts = discussPostMapper.selectDisCussPosts(userId, offset, limit);

        return discussPosts;
    }

    @Override
    public int getDiscussPostRows(int userId) {
        int i = discussPostMapper.selectDiscussPostRows(userId);
        return i;
    }

    @Override
    public int addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null){
            throw new RuntimeException("参数不能为空");
        }
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));


        return discussPostMapper.insertDiscussPost(discussPost);
    }

    public int updateCommentCount(int id, int commentCount) {
        int rows = discussPostMapper.updateDiscussPostCommetnCount(id, commentCount);
        return rows;
    }

    public int selectDiscussPostCount(int userId) {
        int i = discussPostMapper.selectCommentCount(userId);
        return i;
    }
}




