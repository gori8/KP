package com.example.testapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    //@Autowired
    //private DiscoveryClient discoveryClient;

    @Value("${app.id}")
    String  instance;

    @RequestMapping("/")
    public String helloFromTestApi(){
        return "Hello from instance = " + instance;
    }
}

