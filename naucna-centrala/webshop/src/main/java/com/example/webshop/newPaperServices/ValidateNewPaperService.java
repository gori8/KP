package com.example.webshop.newPaperServices;

import com.example.webshop.dto.FormSubmissionDto;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikRepository;
import com.example.webshop.repository.NacinPlacanjaRepository;
import com.example.webshop.repository.NaucnaOblastRepository;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidateNewPaperService  implements JavaDelegate {
    @Autowired
    IdentityService identityService;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        boolean validation = true;

        List<FormSubmissionDto> noviCasopisForma = (List<FormSubmissionDto>)execution.getVariable("noviCasopisForma");

        Casopis casopis = new Casopis();
        casopis.setAktiviran(false);

        for (FormSubmissionDto formField : noviCasopisForma) {
            if(formField.getFieldId().equals("id")){
                casopis.setId(Long.parseLong(formField.getFieldValue(), 10));
            }
            if(formField.getFieldId().equals("naziv")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    System.out.println(1);
                    validation = false;
                    break;
                }
                else {
                    casopis.setNaziv(formField.getFieldValue());
                }
            }
            /*if(formField.getFieldId().equals("clanarina")) {
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    System.out.println(2);
                    validation = false;
                    break;
                }
                else {
                    try{
                        BigDecimal bd = new BigDecimal(formField.getFieldValue());
                        casopis.setClanarina(bd);
                    }catch (Exception e){
                        System.out.println(3);
                        validation = false;
                        break;
                    }
                }
            }*/
            if(formField.getFieldId().equals("komeSeNaplacuje")) {
                if(!formField.getFieldValue().equals("Citalac") && !formField.getFieldValue().equals("Autor")){
                    System.out.println(4);
                    validation = false;
                    break;
                }
                else {
                    casopis.setKomeSeNaplacuje(formField.getFieldValue());
                }
            }

            if(formField.getFieldId().contains("naucnaOblast")){
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    System.out.println(5);
                    validation = false;
                    break;
                }
                else {
                    NaucnaOblast naucnaOblast = naucnaOblastRepository.getOne(Long.parseLong(formField.getFieldValue(), 10));
                    casopis.getNaucneOblasti().add(naucnaOblast);
                    naucnaOblast.getCasopisi().add(casopis);
                    naucnaOblastRepository.save(naucnaOblast);
                }
            }

            if(formField.getFieldId().contains("nacinPlacanja")){
                if(formField.getFieldValue()==null || formField.getFieldValue().trim().equals("")){
                    System.out.println(6);
                    validation = false;
                    break;
                }
                else {
                    NacinPlacanja nacinPlacanja = nacinPlacanjaRepository.getOne(Long.parseLong(formField.getFieldValue(), 10));
                    casopis.getNaciniPlacanja().add(nacinPlacanja);
                    nacinPlacanja.getCasopisi().add(casopis);
                    nacinPlacanjaRepository.save(nacinPlacanja);
                }
            }
        }

        if(casopis.getNaucneOblasti().size()==0){
            validation=false;
        }else{
            String naucneOblasti = "";
            for (NaucnaOblast no:casopis.getNaucneOblasti()) {
                if(naucneOblasti.equals("")){
                    naucneOblasti+=no.getNaziv();
                }else{
                    naucneOblasti+=", "+no.getNaziv();
                }
            }
            execution.setVariable("naucneOblasti",naucneOblasti);
        }

        if(casopis.getNaciniPlacanja().size()==0){
            validation=false;
        }else{
            String naciniPlacanja = "";
            for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
                if(naciniPlacanja.equals("")){
                    naciniPlacanja+=np.getNaziv();
                }else{
                    naciniPlacanja+=", "+np.getNaziv();
                }
            }
            execution.setVariable("naciniPlacanja",naciniPlacanja);
        }

        if(validation==false){
            execution.setVariable("validacija", false);
            return;
        }

        String username = (String)execution.getVariable("starterIdVariable");
        Korisnik glavniUrednik = korisnikRepository.findOneByUsername(username);
        casopis.setGlavniUrednik(glavniUrednik);

        if(casopis.getId()!=null){
            Casopis oldCasopis = casopisRepository.getOne(casopis.getId());
            casopis.setIssn(oldCasopis.getIssn());
            casopis.setRecenzenti(new ArrayList<>());
            casopis.setUrednici(new ArrayList<>());
        }else{
            Long maxIssn = casopisRepository.findMaxIssn();
            if(maxIssn == null){
                maxIssn = 10000000L;
            }
            casopis.setIssn(maxIssn+1);
        }

        casopis = casopisRepository.save(casopis);

        execution.setVariable("validacija", true);
        execution.setVariable("id", casopis.getId());

        System.out.println((Long)execution.getVariable("id"));
    }
}
