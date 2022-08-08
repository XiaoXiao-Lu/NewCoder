package com.kinnon.mapper;

import com.kinnon.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2022-08-02 14:42:07
* @Entity com.kinnon.domain.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectCommentsByEntity(@Param("entityType") int entityType, @Param("entityId")
            int entityId, @Param("offset") int offset, @Param("limit") int limit);

    int selectCountByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);

    int insertCommeent(Comment comment);

}




