package com.example.webshop.services;

import com.example.webshop.dto.ElementDTO;
import com.example.webshop.dto.NaucnaOblastDTO;
import com.example.webshop.dto.RadDTO;
import com.example.webshop.dto.RadFoundDTO;

import java.util.List;

public interface RadService {
    List<NaucnaOblastDTO> getNaucneOblastiByIzdanjeId(Long izdanjeId);

    Long addScientificWork(RadDTO dto);

    List<RadFoundDTO> simpleQuery(ElementDTO elements);

    List<RadFoundDTO> boolQuery(List<ElementDTO> elements);
}
