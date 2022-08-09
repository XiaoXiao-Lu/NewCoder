package com.kinnon;

import com.kinnon.domain.Message;
import com.kinnon.mapper.MessageMapper;
import com.kinnon.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Kinnon
 * @create 2022-08-08 21:26
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class Messagetest {

    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void test(){


    }

}
