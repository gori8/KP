package com.example.webshop.services;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.CorrectionDTO;
import com.example.webshop.dto.UredniciRecenzentiDTO;

import java.util.List;

public interface UserService {
    public UredniciRecenzentiDTO getUrednikRecenzent(Long casopisId, String username);
    public CorrectionDTO getCorrectionData(Long casopisId);
    public List<CasopisDTO> getMyPapers(String username);
    public List<CasopisDTO> getAllPapers();
}
