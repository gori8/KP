package rs.ac.uns.naucnacentrala.camunda.tasks.papers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rs.ac.uns.naucnacentrala.dto.CasopisPV;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.CasopisRepository;
import rs.ac.uns.naucnacentrala.repository.UserRepository;
import rs.ac.uns.naucnacentrala.service.PaymentService;

import java.util.Date;

@Service
public class ProveraPretplateTask implements JavaDelegate {


    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentService paymentService;


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        CasopisPV casopisPV = (CasopisPV) execution.getVariable("casopis");
        Casopis casopis = casopisRepository.getOne(casopisPV.getId());
        User autor = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        boolean flag = false;
        Plan plan = null;

        for(Pretplata pretplata : autor.getPretplate()){
            if(pretplata.getPlan().getCasopis().getId()==casopis.getId()){
                if(!pretplata.getDatumIsticanja().before(new Date())){
                    flag = true;
                    plan = pretplata.getPlan();
                    break;
                }
            }
        }
        if(flag){
            execution.setVariable("autor_pretplacen",true);
        }else{
            execution.setVariable("autor_pretplacen",false);
            String url = paymentService.getRedirectUrl(plan.getUuid().toString(),autor.getUsername(),execution.getProcessInstanceId());
            execution.setVariable("urlToPay",url);
        }
    }

}
