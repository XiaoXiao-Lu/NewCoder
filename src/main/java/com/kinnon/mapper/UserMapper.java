package com.kinnon.mapper;

import com.kinnon.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Thinkpad
* @description 针对表【user】的数据库操作Mapper
* @createDate 2022-08-02 01:08:10
* @Entity com.kinnon.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




