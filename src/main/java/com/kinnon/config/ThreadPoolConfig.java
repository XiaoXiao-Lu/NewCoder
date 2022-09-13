package com.kinnon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Kinnon
 * @create 2022-09-11 18:05
 */
@Configuration
@EnableScheduling
@EnableAsync
public class ThreadPoolConfig {
}
