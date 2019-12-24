package com.example.demo.controller;

import com.example.demo.dto.KupovinaDTO;
import com.example.demo.model.Kupovina;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@CrossOrigin("http://localhost:4200")
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
public class DemoController {

    @Autowired
    DemoService demoService;

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public ResponseEntity<Kupovina> kupiCasopis(@RequestBody KupovinaDTO kupovinaDTO) {

        return new ResponseEntity<Kupovina>(demoService.kupiCasopis(kupovinaDTO), HttpStatus.OK);

    }
}
