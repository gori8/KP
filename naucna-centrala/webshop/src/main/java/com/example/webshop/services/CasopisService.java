package com.example.webshop.services;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.IzdanjeDTO;
import com.example.webshop.dto.TaskLinkDTO;

import java.util.List;

public interface CasopisService {
    public List<TaskLinkDTO> getTasks(String username);

    public CasopisDTO getPaper(Long id);

    public CasopisDTO getNumbersForPaper(Long id);

    public List<CasopisDTO> getBoughtItems(String username);

    public List<IzdanjeDTO> getBoughtItemsForCasopis(String username, Long casopisId);
}
