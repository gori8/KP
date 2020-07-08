package com.example.webshop.services;

import com.example.webshop.dto.*;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface RadService {
    List<NaucnaOblastDTO> getNaucneOblastiByIzdanjeId(Long izdanjeId);

    Long addScientificWork(RadDTO dto);

    List<RadFoundDTO> simpleQuery(ElementDTO elements);

    List<RadFoundDTO> boolQuery(List<ElementDTO> elements);

    List<RecenzentDTO> getRecenzenteForNaucnaOblast(Long id);

    Long finsihAddingScientificWork(FinishAddingRadDTO dto);

    List<RecenzentDTO> geoSearch(Long radId);

    List<RecenzentDTO> moreLikeThis(Long radId);
}
