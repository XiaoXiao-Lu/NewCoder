package com.kinnon.mapper;

import com.kinnon.domain.LoginTicket;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Thinkpad
* @description 针对表【login_ticket】的数据库操作Mapper
* @createDate 2022-08-02 14:42:16
* @Entity com.kinnon.domain.LoginTicket
*/
@Mapper

public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

}




