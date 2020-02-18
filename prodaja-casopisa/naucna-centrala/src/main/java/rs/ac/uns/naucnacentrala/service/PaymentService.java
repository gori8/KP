package rs.ac.uns.naucnacentrala.service;


import rs.ac.uns.naucnacentrala.dto.PlanDTO;

public interface PaymentService {

    void createLinks(PlanDTO planDTO, Long casopisId) throws Exception;

    String completeRegistration(String uuid, Long nacinPlacanjaId);

    String getRedirectUrl(String uapId, String username, String processInstanceId);

    String changePayed(String uuid, Boolean success, String username, String processInstanceId);
}
