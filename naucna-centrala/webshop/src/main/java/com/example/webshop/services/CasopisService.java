package com.example.webshop.services;

import com.example.webshop.dto.*;
import com.example.webshop.model.Plan;

import java.util.List;

public interface CasopisService {
    public List<TaskLinkDTO> getTasks(String username);

    public CasopisDTO getPaper(Long id);

    public CasopisDTO getNumbersForPaper(Long id);

    public List<CasopisDTO> getBoughtItems(String username);

    public List<IzdanjeDTO> getBoughtItemsForCasopis(String username, Long casopisId);

    public List<Plan> setPlans(List<PlanDTO> dto, Long casopisId);

    public List<PlanDTO> getPlansForPaper(Long casopisId);

    public List<PretplaceniCasopisDTO> getBoughtPapers(String username);

    public List<TaskLinkDTO> getTasksPlans(String username);
}
