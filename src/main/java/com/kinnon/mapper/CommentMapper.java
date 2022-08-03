package com.kinnon.mapper;

import com.kinnon.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Thinkpad
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2022-08-02 14:42:07
* @Entity com.kinnon.domain.Comment
*/
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




