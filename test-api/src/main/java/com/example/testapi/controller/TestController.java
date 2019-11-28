package com.example.testapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@RestController
public class TestController {
    //@Autowired
    //private DiscoveryClient discoveryClient;

    @Value("${app.id}")
    String  instance;

    @RequestMapping("/{message}")
    public String helloFromTestApi(@PathVariable("message") String m){
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String myUrl = "jdbc:mysql://mysqlmaster:3306/test";

            Connection conn = DriverManager.getConnection(myUrl, "root", "root");


            // the mysql insert statement
            String query = " insert into testtable (message, instance)"
                    + " values ( ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, m);
            preparedStmt.setString (2, instance);


            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
        return "Message = " + m +"    Instance = "+instance;
    }
}

