package com.kinnon;

import com.kinnon.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author Kinnon
 * @create 2022-08-03 22:34
 */

@SpringBootTest
public class MailTest {

    @Autowired
    MailClient mailClient ;
    @Test
    public void test1(){

        mailClient.sendMail("627074396@qq.com","Hello","hahahha");
    }
}
