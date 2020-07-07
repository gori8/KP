package com.example.webshop.controller;


import com.example.webshop.dto.ElementDTO;
import com.example.webshop.dto.NaucnaOblastDTO;
import com.example.webshop.dto.RadDTO;
import com.example.webshop.dto.RadFoundDTO;
import com.example.webshop.services.RadService;
import com.example.webshop.util.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/scientific-work")
@CrossOrigin("*")
public class RadController {

    @Autowired
    RadService radService;

    @Autowired
    StorageService storageService;

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/naucneOblasti/{izdanjeId}", method = RequestMethod.GET)
    public ResponseEntity<?> getNaucneOblastiByIzdanjeId(@PathVariable("izdanjeId")Long izdanjeId) {
        return ResponseEntity.ok(radService.getNaucneOblastiByIzdanjeId(izdanjeId));
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/pdf", method = RequestMethod.POST,consumes = "multipart/form-data")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {

        String name = storageService.store(file);

        return new ResponseEntity<String>("\""+name+"\"", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> addScientificWork(@RequestBody RadDTO dto){

        return new ResponseEntity<Long>(radService.addScientificWork(dto),HttpStatus.OK);
    }

    @RequestMapping(value = "/simple" ,method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RadFoundDTO>> simpleQuery(@RequestBody ElementDTO element){

        return new ResponseEntity<List<RadFoundDTO>>(radService.simpleQuery(element),HttpStatus.OK);
    }

    @RequestMapping(value = "/bool" ,method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RadFoundDTO>> boolQuery(@RequestBody List<ElementDTO> elements){

        return new ResponseEntity<List<RadFoundDTO>>(radService.boolQuery(elements),HttpStatus.OK);
    }

}
