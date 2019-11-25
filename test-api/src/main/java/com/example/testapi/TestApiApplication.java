package com.example.testapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TestApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApiApplication.class, args);
    }

}
