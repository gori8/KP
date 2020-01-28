package com.example.userandpaymentinfo.controller;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.Item;
import com.example.userandpaymentinfo.service.InfoService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Registration;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> getAllCasopisi() {

        return new ResponseEntity<List<Item>>(infoService.getAllCasopisi(), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public ResponseEntity<Item> editCasopis(@RequestBody CasopisDTO casopisDTO) throws Exception {

        return new ResponseEntity<Item>(infoService.editCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Item> updateCasopis(@RequestBody CasopisDTO casopisDTO, @PathVariable("id") Long id) {

        return new ResponseEntity<Item>(infoService.updateCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/method/{casopisId}", method = RequestMethod.GET)
    public ResponseEntity<List<NacinPlacanjaDTO>> getNacinePlacanjaZaCasopis(@PathVariable("casopisId") String casopisId) {

        return new ResponseEntity<List<NacinPlacanjaDTO>>(infoService.getNacinePlacanjaZaCasopis(casopisId), HttpStatus.OK);

    }

    @RequestMapping(value = "/url", method = RequestMethod.POST)
    public ResponseEntity<UrlDTO> getUrl(@RequestBody RedirectUrlDTO redirectUrlDTO) {

        return new ResponseEntity<>(infoService.getUrl(redirectUrlDTO),HttpStatus.OK);
    }

    @RequestMapping(value = "/amountandurl/{id}", method = RequestMethod.GET)
    public ResponseEntity<AmountAndUrlDTO> getUrl(@PathVariable("id") String id) {
        return new ResponseEntity<>(infoService.getAmountAndUrl(id),HttpStatus.OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody CreateLinksDTO dto) {

        ReturnLinksDTO ret =infoService.register(dto);

        if(ret.getLinks().isEmpty()){
            return ResponseEntity.status(201).body(ret);
        }

        return new ResponseEntity<>(ret,HttpStatus.OK);
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public ResponseEntity<ReturnLinksDTO> addItem(@RequestBody CreateLinksDTO dto) {

        return new ResponseEntity<>(infoService.register(dto),HttpStatus.OK);
    }

    @RequestMapping(value = "/registration/complete", method = RequestMethod.POST)
    public ResponseEntity<String> registrationCompleted(@RequestBody RegistrationCompletedDTO dto) {
        return new ResponseEntity<String>("\""+infoService.registrationCompleted(dto)+"\"",HttpStatus.OK);
    }

    @RequestMapping(value = "/form/{folder}/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<Object> getForm(@PathVariable("folder") String folder,@PathVariable("uuid") String uuid) {
        return new ResponseEntity<>(infoService.getForm(folder,uuid),HttpStatus.OK);
    }

    @RequestMapping(value = "/image/{folder}/{name}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getImage(@PathVariable("folder") String folder, @PathVariable("name") String name){
        return new ResponseEntity<>(infoService.getImage(folder,name),HttpStatus.OK);
    }
}
