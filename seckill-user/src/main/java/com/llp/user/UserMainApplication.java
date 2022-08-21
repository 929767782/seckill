package com.llp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class, args);
    }

}
