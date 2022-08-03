package com.kinnon.mapper;

import com.kinnon.domain.DiscussPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Thinkpad
* @description 针对表【discuss_post】的数据库操作Mapper
* @createDate 2022-08-02 14:42:03
* @Entity com.kinnon.domain.DiscussPost
*/
@Mapper
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    /**
     * 查询讨论帖子
     * @param userId 用户id
     * @param offset 分页起始页
     * @param limit  分页返回的数据数
     * @return
     */
    List<DiscussPost> selectDisCussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    int selectDiscussPostRows(@Param("userId") int userId);

}




