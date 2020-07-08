package com.example.webshop.controller;


import com.example.webshop.dto.*;
import com.example.webshop.services.RadService;
import com.example.webshop.util.storage.StorageFileNotFoundException;
import com.example.webshop.util.storage.StorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @RequestMapping(method = RequestMethod.PUT)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> finishAddingScientificWork(@RequestBody FinishAddingRadDTO dto){

        return new ResponseEntity<Long>(radService.finsihAddingScientificWork(dto),HttpStatus.OK);
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/pdf/{filename}")
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) throws IOException {
        System.out.println("Filename: "+filename);
        String realFilename=filename.replace("+"," ");
        Resource file = storageService.loadAsResource(realFilename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream")).body(IOUtils.toByteArray(file.getInputStream()));
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/recenzenti/{id}")
    public ResponseEntity<List<RecenzentDTO>> getRecenzenteForNaucnaOblast(@PathVariable Long id) throws IOException {
        return new ResponseEntity<List<RecenzentDTO>>(radService.getRecenzenteForNaucnaOblast(id),HttpStatus.OK);
    }

    @RequestMapping(value = "/geo/{radId}" ,method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecenzentDTO>> geoSearch(@PathVariable Long radId){

        return new ResponseEntity<List<RecenzentDTO>>(radService.geoSearch(radId),HttpStatus.OK);
    }

    @RequestMapping(value = "/moreLikeThis/{radId}" ,method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RecenzentDTO>> moreLikeThis(@PathVariable Long radId){

        return new ResponseEntity<List<RecenzentDTO>>(radService.moreLikeThis(radId),HttpStatus.OK);
    }

}
