package com.example.webshop.services;

import com.example.webshop.dto.TaskLinkDTO;

import java.util.List;

public interface CasopisService {
    public List<TaskLinkDTO> getTasks(String username);
}
