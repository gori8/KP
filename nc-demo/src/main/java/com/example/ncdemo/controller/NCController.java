package com.example.ncdemo.controller;

import com.example.ncdemo.dto.CasopisDTO;
import com.example.ncdemo.service.CasopisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/nc")
public class NCController {

    @Autowired
    CasopisService casopisService;

    @RequestMapping(value = "/casopis", method = RequestMethod.GET)
    public ResponseEntity<List<CasopisDTO>> getAllCasopis() {
        return new ResponseEntity<List<CasopisDTO>>(casopisService.getAllCasopis(), HttpStatus.OK);
    }

    @RequestMapping(value = "/casopis/{id}", method = RequestMethod.GET)
    public ResponseEntity<CasopisDTO> getCasopis(@PathVariable Long id) {
        return new ResponseEntity<CasopisDTO>(casopisService.getCasopis(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/pay/{uapId}", method = RequestMethod.POST)
    public ResponseEntity<String> pay(@PathVariable String uapId ) {

        String url = casopisService.getRedirectUrl(uapId);
        return new ResponseEntity<>("\""+url+"\"",HttpStatus.OK);
    }
}
