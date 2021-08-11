package com.pipms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @ClassName AopConfig
 * @Description TODO
 * @Author 661595
 * @Date 2021/7/1515:23
 * @Version 1.0
 **/
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class AopConfig {
}
