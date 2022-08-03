package com.kinnon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinnon.domain.Comment;
import com.kinnon.service.CommentService;
import com.kinnon.mapper.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author Thinkpad
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2022-08-02 14:42:07
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




