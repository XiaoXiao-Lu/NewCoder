package com.kinnon.mapper;

import com.kinnon.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Thinkpad
* @description 针对表【message】的数据库操作Mapper
* @createDate 2022-08-02 14:42:19
* @Entity com.kinnon.domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




