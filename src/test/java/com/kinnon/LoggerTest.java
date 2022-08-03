package com.kinnon;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Kinnon
 * @create 2022-08-03 15:33
 */

@SpringBootTest
public class LoggerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void test1(){
        logger.debug("debug-----");
        logger.info("info------");
        logger.warn("warning----");
        logger.error("error-----");
    }

}
