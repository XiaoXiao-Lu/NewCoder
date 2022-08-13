package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.Comment;
import com.kinnon.domain.DiscussPost;
import com.kinnon.service.CommentService;
import com.kinnon.mapper.CommentMapper;
import com.kinnon.service.DiscussPostService;
import com.kinnon.util.NewCoderConstant;
import com.kinnon.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-08-02 14:42:07
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService, NewCoderConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> selectCommentList(int entityType, int entityId, int offset, int limit) {
        List<Comment> comments = commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
        return comments;
    }

    public int selectCountByEntity(int entityType, int entityId) {
        int count = commentMapper.selectCountByEntity(entityType, entityId);
        return count;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        int i = commentMapper.insertCommeent(comment);

        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int commentCount = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), commentCount);

        }



        return i;
    }

    @Override
    public List<Comment> selectCommentListByUserId(int userId) {
        return commentMapper.selectByUserId(userId);
    }

    @Override
    public List<Comment> selectCommentListByUserIdAndEntityType(int userId, int entityType) {
        return commentMapper.selectByUserIdAndEntityType(userId, entityType);
    }


}




