package com.example.userandpaymentinfo.controller;

import com.example.userandpaymentinfo.dto.*;
import com.example.userandpaymentinfo.model.Casopis;
import com.example.userandpaymentinfo.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin("http://localhost:4200")
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET)
    public ResponseEntity<List<Casopis>> getAllCasopisi() {

        return new ResponseEntity<List<Casopis>>(infoService.getAllCasopisi(), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public ResponseEntity<Casopis> editCasopis(@RequestBody CasopisDTO casopisDTO) throws Exception {

        return new ResponseEntity<Casopis>(infoService.editCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Casopis> updateCasopis(@RequestBody CasopisDTO casopisDTO, @PathVariable("id") Long id) {

        return new ResponseEntity<Casopis>(infoService.updateCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/methods/{casopisId}", method = RequestMethod.GET)
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
}
