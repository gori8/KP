package com.example.ncdemo.service;

import com.example.ncdemo.dto.CasopisDTO;

import java.util.List;

public interface CasopisService {
    public CasopisDTO getCasopis(Long id);
    public List<CasopisDTO> getAllCasopis();
    public String getRedirectUrl(String uapId);
}
