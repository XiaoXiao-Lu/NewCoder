package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.DiscussPost;
import com.kinnon.domain.User;
import com.kinnon.service.DiscussPostService;
import com.kinnon.mapper.DiscussPostMapper;
import com.kinnon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}




