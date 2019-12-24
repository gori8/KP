package com.example.userandpaymentinfo.controller;

import com.example.userandpaymentinfo.dto.CasopisDTO;
import com.example.userandpaymentinfo.dto.PodaciORacunuDTO;
import com.example.userandpaymentinfo.model.Casopis;
import com.example.userandpaymentinfo.model.PodaciORacunu;
import com.example.userandpaymentinfo.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin("http://localhost:4200")
@RequestMapping(value = "api", produces = "application/json;charset=UTF-8")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(value = "/journal", method = RequestMethod.GET)
    public ResponseEntity<List<Casopis>> getAllCasopisi() {

        return new ResponseEntity<List<Casopis>>(infoService.getAllCasopisi(), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal", method = RequestMethod.POST)
    public ResponseEntity<Casopis> addCasopis(@RequestBody CasopisDTO casopisDTO) {

        return new ResponseEntity<Casopis>(infoService.addCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Casopis> updateCasopis(@RequestBody CasopisDTO casopisDTO, @PathVariable("id") Long id) {

        return new ResponseEntity<Casopis>(infoService.updateCasopis(casopisDTO), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}/paymentmethod", method = RequestMethod.GET)
    public ResponseEntity<List<PodaciORacunu>> getSveRacuneJednogCasopisa(@PathVariable("id") Long id) {

        return new ResponseEntity<List<PodaciORacunu>>(infoService.getSveRacuneJednogCasopisa(id), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}/paymentmethod", method = RequestMethod.POST)
    public ResponseEntity<PodaciORacunu> addPodaciORacunu(@RequestBody PodaciORacunuDTO podaciORacunuDTO, @PathVariable("id") Long id) {

        return new ResponseEntity<PodaciORacunu>(infoService.addPodaciORacunu(podaciORacunuDTO, id), HttpStatus.OK);

    }

    @RequestMapping(value = "/journal/{id}/paymentmethod/{idPayment}", method = RequestMethod.PUT)
    public ResponseEntity<PodaciORacunu> updatePodaciORacunu(@RequestBody PodaciORacunuDTO podaciORacunuDTO, @PathVariable("id") Long id,
                                                             @PathVariable("idPayment") Long idPayment) {

        return new ResponseEntity<PodaciORacunu>(infoService.updatePodaciORacunu(podaciORacunuDTO, idPayment), HttpStatus.OK);

    }



}
