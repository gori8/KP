package com.example.userandpaymentinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;


@SpringBootApplication
@EnableDiscoveryClient
public class UserandpaymentinfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserandpaymentinfoApplication.class, args);
    }



}
