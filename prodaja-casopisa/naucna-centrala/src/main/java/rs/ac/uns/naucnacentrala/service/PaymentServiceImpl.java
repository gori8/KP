package rs.ac.uns.naucnacentrala.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.url.UrlClass;
import rs.ac.uns.naucnacentrala.dto.*;
import rs.ac.uns.naucnacentrala.model.*;
import rs.ac.uns.naucnacentrala.repository.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;



import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepository korisnikRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PretplataRepository pretplataRepository;

    @Override
    public void createLinks(PlanDTO planDTO,Long casopisId) throws Exception {
        Casopis casopis = casopisRepository.getOne(casopisId);
        User user = korisnikRepository.findByUsername(casopis.getGlavniUrednik());

        Plan plan = new Plan();
        plan.setPeriod(planDTO.getPeriod());
        plan.setUcestalostPerioda(planDTO.getUcestalostPerioda());
        plan.setCena(planDTO.getCena());
        plan.setCasopis(casopis);
        casopis.getPlanovi().add(plan);
        plan = planRepository.save(plan);
        casopis = casopisRepository.save(casopis);


        ItemDTO dto = new ItemDTO();
        dto.setNaziv(plan.getCasopis().getNaziv()+", plan: "+plan.getPeriod()+" "+plan.getUcestalostPerioda());
        dto.setRedirectUrl(UrlClass.REDIRECT_URL_REGISTRATION);
        dto.setNaciniPlacanja(new ArrayList<>());
        dto.setEmail(user.getEmail());
        dto.setAmount(plan.getCena());
        for (NacinPlacanja np:plan.getCasopis().getNaciniPlacanja()) {
            dto.getNaciniPlacanja().add(np.getId());
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ItemDTO> entity = new HttpEntity<ItemDTO>(dto, headers);
        ResponseEntity<ReturnLinksDTO> response=null;

        try {
            response =
                    restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL + "add", entity, ReturnLinksDTO.class);
            System.out.println("STATUS CODE: " + response.getStatusCodeValue());
        }catch(HttpClientErrorException e) {
            //e.printStackTrace();
            if (e.getStatusCode() == HttpStatus.valueOf(401))
            {
                System.out.println("User is not registered");
                System.out.println("Trying to register user...");
                ReturnLinksDTO responseBody = objectMapper.readValue(e.getResponseBodyAsString(),ReturnLinksDTO.class);
                HttpHeaders headersReg = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                RegisterDTO registerDTO = new RegisterDTO();
                registerDTO.setEmail(user.getEmail());
                registerDTO.setUrl(UrlClass.REDIRECT_URL_REAL_REGISTRATION+plan.getId());
                HttpEntity<RegisterDTO> entityReg = new HttpEntity<RegisterDTO>(registerDTO, headersReg);
                ResponseEntity<Boolean> responseReg = restTemplate.postForEntity(responseBody.getRegisterUrl(), entityReg, Boolean.class);
                if (responseReg.getBody()) {
                    System.out.println("User registered successfully");
                    response =
                            restTemplate.postForEntity(UrlClass.USER_AND_PAYMENT_URL + "add", entity, ReturnLinksDTO.class);
                } else {
                    throw new Exception();
                }
            }
        }
        ReturnLinksDTO responseBody = response.getBody();

        plan.setUuid(UUID.fromString(responseBody.getUuid()));

        if(response.getStatusCode().value()==200) {
            for (LinkDTO linkDTO : responseBody.getLinks()) {
                boolean flag = false;
                for (Link l:plan.getCasopis().getLinkovi()) {
                    if(l.getNacinPlacanja()==linkDTO.getNacinPlacanjaId()){
                        flag = true;
                        break;
                    }
                }

                if(flag){
                    continue;
                }

                Link link = new Link();
                link.setCasopis(plan.getCasopis());
                link.setUrl(linkDTO.getLink());
                link.setCompleted(false);
                link.setNacinPlacanja(linkDTO.getNacinPlacanjaId());
                link = linkRepository.save(link);

                plan.getCasopis().getLinkovi().add(link);
            }
        }

        casopisRepository.save(plan.getCasopis());
    }


    @Override
    public String completeRegistration(String uuid, Long nacinPlacanjaId){
        Plan plan = planRepository.findOneByUuid(UUID.fromString(uuid));
        Casopis casopis = plan.getCasopis();
        Link link = linkRepository.findOneByCasopisUuidAndNacinPlacanja(plan.getCasopis().getId(),nacinPlacanjaId);
        link.setCompleted(true);
        linkRepository.save(link);

        boolean flag=true;
        for(Link l : casopis.getLinkovi()){
            if(!l.getCompleted()){
                flag=false;
                break;
            }
        }

        if(flag){
            casopis.setCasopisStatus(CasopisStatus.WAITING_FOR_APPROVAL);
            casopisRepository.save(casopis);
        }

        return UrlClass.FRON_WEBSHOP+"urednik/journals";
    }


    @Override
    public String getRedirectUrl(String uapId,String username,String processInstanceId){
        ObjectMapper mapper = new ObjectMapper();

        MappingClass mc = new MappingClass();
        mc.setRedirectUrl(UrlClass.REDIRECT_URL_PAYMENT_IGOR+username+"/"+uapId+"/"+processInstanceId);
        mc.setId(uapId);

        String json="";

        try {
            json = mapper.writeValueAsString(mc);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        ResponseEntity<String> response
                = restTemplate.postForEntity(UrlClass.DOBAVI_KP_FRONT_URL_SA_NACINIMA_PLACANJA_FROM_PAYMENT_INFO,entity,String.class);

        JSONObject actualObj=null;
        String ret = "";

        try {
            actualObj = new JSONObject(response.getBody());
            ret = actualObj.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public String changePayed(String uuid, Boolean success, String username, String processInstanceId) {
        if (success) {
            User korisnik = korisnikRepository.findByUsername(username);

            Plan plan = planRepository.findOneByUuid(UUID.fromString(uuid));
            if(!processInstanceId.equals("stagod")) {


                if(plan==null){
                    return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
                }

                Pretplata pretplata = new Pretplata();

                Date date = new Date();
                Long dayMilis = 1000L * 60 * 60 * 24;

                switch (plan.getPeriod()){
                    case "DAY" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() ));
                    } break;
                    case "WEEK" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 7 ));
                    } break;
                    case "MONTH" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 30 ));
                    } break;
                    case "YEAR" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 365));
                    } break;
                }

                Long time = date.getTime();
                date = new Date(time - time % (24 * 60 * 60 * 1000));
                date = new Date(date.getTime() + 23 * 60 * 60 * 1000);

                pretplata.setDatumIsticanja(date);
                pretplata.setPlan(plan);
                pretplata.setPretplatnik(korisnik);
                pretplata = pretplataRepository.save(pretplata);
                korisnik.getPretplate().add(pretplata);
                korisnikRepository.save(korisnik);
                plan.getPretplate().add(pretplata);
                planRepository.save(plan);

                Task task = taskService.createTaskQuery().active().processInstanceId(processInstanceId).taskDefinitionKey("placanje_pretplate").singleResult();

                if (task != null) {
                    taskService.complete(task.getId());
                }

                return UrlClass.FRON_WEBSHOP + "input/paper/" + processInstanceId;
            }else{

                if(plan==null){
                    return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
                }

                Pretplata pretplata = new Pretplata();

                Date date = new Date();
                Long dayMilis = 1000L * 60 * 60 * 24;

                switch (plan.getPeriod()){
                    case "DAY" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() ));
                    } break;
                    case "WEEK" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 7 ));
                    } break;
                    case "MONTH" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 30 ));
                    } break;
                    case "YEAR" :{
                        date = new Date(date.getTime() + ( dayMilis * plan.getUcestalostPerioda() * 365));
                    } break;
                }

                Long time = date.getTime();
                date = new Date(time - time % (24 * 60 * 60 * 1000));
                date = new Date(date.getTime() + 23 * 60 * 60 * 1000);

                pretplata.setDatumIsticanja(date);
                pretplata.setPlan(plan);
                pretplata.setPretplatnik(korisnik);
                pretplata = pretplataRepository.save(pretplata);
                korisnik.getPretplate().add(pretplata);
                korisnikRepository.save(korisnik);
                plan.getPretplate().add(pretplata);
                planRepository.save(plan);

                return UrlClass.FRON_WEBSHOP+"paymentresponse/success";
            }
        } else {
            return UrlClass.FRON_WEBSHOP+"paymentresponse/failed";
        }
    }



}
