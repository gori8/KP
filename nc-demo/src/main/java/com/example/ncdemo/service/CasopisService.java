package com.example.ncdemo.service;

import com.example.ncdemo.dto.CasopisDTO;

import java.util.List;

public interface CasopisService {
    public CasopisDTO getCasopis(String id);
    public List<CasopisDTO> getAllCasopis();
    public String getRedirectUrl(String uapId);
    public String changePayed(String uuid, Boolean success);
}
