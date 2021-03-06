package rs.ac.uns.naucnacentrala.camunda.tasks.journals.add;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.camunda.service.JournalService;
import rs.ac.uns.naucnacentrala.dto.CasopisDTO;
import rs.ac.uns.naucnacentrala.dto.KoautorDTO;
import rs.ac.uns.naucnacentrala.dto.PlanDTO;
import rs.ac.uns.naucnacentrala.model.Casopis;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;
import rs.ac.uns.naucnacentrala.model.Link;
import rs.ac.uns.naucnacentrala.model.User;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.CasopisService;
import rs.ac.uns.naucnacentrala.service.PaymentService;
import rs.ac.uns.naucnacentrala.utils.ObjectMapperUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Service
public class ActivateJournalValidationTask implements JavaDelegate {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    PaymentService paymentService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        boolean flag_val=true;
        Casopis casopis=casopisRepository.findByProcessInstanceId(execution.getProcessInstanceId());
        ArrayList<String> userIds= (ArrayList<String>) execution.getVariable("recezentiSel");
        ArrayList<String> uredniciIds= (ArrayList<String>) execution.getVariable("uredniciSel");
        if(userIds.size()<2){
            flag_val=false;
            HashMap<String,String> valErros=new HashMap<>();
            valErros.put("recezentiSel","You have to select at least 2 reviewers");
            execution.setVariable("validationErrors",valErros);
        }else {
            for (String id : userIds) {
                User user = userRepository.getOne(Long.valueOf(id));
                user.getRecenziramCasopise().add(casopis);
                casopis.getRecezenti().add(user);
                casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
                casopisRepository.save(casopis);
            }

            for (String id : uredniciIds) {
                User user = userRepository.getOne(Long.valueOf(id));
                user.getUredjujemCasopise().add(casopis);
                casopis.getUrednici().add(user);
                casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
                casopisRepository.save(casopis);
            }

        }
        execution.setVariable("flag_val",flag_val);
        String plansJson = execution.getVariable("planovi").toString();
        ArrayList<PlanDTO> plans = objectMapper.readValue(plansJson,new TypeReference<ArrayList<PlanDTO>>(){});
        for (PlanDTO planDTO : plans) {
            paymentService.createLinks(planDTO, casopis.getId());
        }
        for(Link link : casopis.getLinkovi()){
            if(!link.getCompleted()){
                casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_INPUT);
                casopisRepository.save(casopis);
                break;
            }
        }
    }

}
