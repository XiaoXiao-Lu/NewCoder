package com.kinnon.service;

import com.kinnon.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【comment】的数据库操作Service
* @createDate 2022-08-02 14:42:07
*/
public interface CommentService extends IService<Comment> {

    public List<Comment> selectCommentList(int entityType, int entityId, int offset, int limit) ;

    public int selectCountByEntity(int entityType, int entityId) ;

    public int insertComment(Comment comment) ;

    public List<Comment> selectCommentListByUserId(int userId);

    public List<Comment> selectCommentListByUserIdAndEntityType(int userId, int entityType);

    }
