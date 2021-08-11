package com.pipms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName pipmsApplication
 * @Description TODO
 * @Author 661595
 * @Date 2021/6/219:46
 * @Version 1.0
 **/
@SpringBootApplication
@EnableTransactionManagement
public class pipmsApplication extends SpringBootServletInitializer{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(pipmsApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(pipmsApplication.class);
    }
}
