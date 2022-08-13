package com.kinnon.mapper;

import com.kinnon.domain.LoginTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

/**
* @author Thinkpad
* @description 针对表【login_ticket】的数据库操作Mapper
* @createDate 2022-08-02 14:42:16
* @Entity com.kinnon.domain.LoginTicket
*/
@Mapper
@Deprecated
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

    @Insert({"insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys =true,keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    @Update({
            "update login_ticket set status=#{status} where ticket = #{ticket}"
    })
    int updateLoginTicket(@Param("ticket") String ticket, @Param("status") int status);



}




