package com.example.webshop.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String id;
    private String instanceId;
    private String processId;
    private String user;
    private String glavniUrednik;
    private String recenzenti;
    private String urednici;
    private String naziv;
    private Long clanarina;
    private String komeSeNaplacuje;
    private String naciniPlacanja;
    private String naucneOblasti;
}
