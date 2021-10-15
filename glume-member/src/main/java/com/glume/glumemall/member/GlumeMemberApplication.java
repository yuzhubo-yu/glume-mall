package com.glume.glumemall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients(basePackages = "com.glume.glumemall.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GlumeMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GlumeMemberApplication.class, args);
    }

}