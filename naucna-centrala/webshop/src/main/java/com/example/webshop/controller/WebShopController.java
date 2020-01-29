package com.example.webshop.controller;

import com.example.webshop.dto.*;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.NacinPlacanjaRepository;
import com.example.webshop.repository.NaucnaOblastRepository;
import com.example.webshop.services.CasopisService;
import com.example.webshop.services.KPService;
import com.example.webshop.services.UserService;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/webshop")
@CrossOrigin("*")
public class WebShopController {
    @Autowired
    IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    FormService formService;

    @Autowired
    NaucnaOblastRepository naucnaOblastRepository;

    @Autowired
    NacinPlacanjaRepository nacinPlacanjaRepository;

    @Autowired
    UserService userService;

    @Autowired
    KPService kpService;

    @Autowired
    CasopisService casopisService;

    @Autowired
    CasopisRepository casopisRepository;

    @GetMapping(path = "/registration", produces = "application/json")
    public @ResponseBody String registrationStart() {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("registracija");

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

        return "\""+task.getId()+"\"";
    }

    @GetMapping(path = "/newPaper/{username}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody String newPaperStart(@PathVariable String username) {
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("noviCasopis");
        runtimeService.setVariable(pi.getId(), "starterIdVariable", username);

        return "\""+pi.getId()+"\"";
    }

    @PostMapping(path = "/newPaper/{processId}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody String newPaperSubmit(@RequestBody List<FormSubmissionDto> dto, @PathVariable String processId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        runtimeService.setVariable(processId, "noviCasopisForma", dto);
        formService.submitTaskForm(task.getId(), map);

        return "\""+processId+"\"";
    }

    @PostMapping(path = "/newPaper/uredniciRecenzenti/{processId}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody Boolean newPaperUredniciRecenzentiSubmit(@RequestBody UredniciRecenzentiDTO dto, @PathVariable String processId) {

        if(dto.getRecenzenti().size() < 2){
            return false;
        }

        Long casopisId = (Long)runtimeService.getVariable(processId,"id");

        kpService.createLinks(casopisId);

        HashMap<String, Object> map = new HashMap<>();

        map.put("urednici",dto.getUrednici());
        map.put("recenzenti",dto.getRecenzenti());

        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        runtimeService.setVariable(processId, "uredniciRecenzenti", dto);
        formService.submitTaskForm(task.getId(), map);

        return true;
    }

    @GetMapping(path = "/sciencefields", produces = "application/json")
    public @ResponseBody List<NaucnaOblastDTO> getNaucneOblasti(){
        List<NaucnaOblast> noList = naucnaOblastRepository.findAll();

        List<NaucnaOblastDTO> ret = new ArrayList<>();
        for (NaucnaOblast no : noList) {
            NaucnaOblastDTO noDTO = new NaucnaOblastDTO();
            noDTO.setId(no.getId());
            noDTO.setNaziv(no.getNaziv());

            ret.add(noDTO);
        }

        return ret;
    }

    @GetMapping(path = "/payments", produces = "application/json")
    public @ResponseBody List<NacinPlacanjaDTO> getNacinePlacanja(){
        List<NacinPlacanja> npList = nacinPlacanjaRepository.findAll();

        List<NacinPlacanjaDTO> ret = new ArrayList<>();
        for (NacinPlacanja np : npList) {
            NacinPlacanjaDTO npDTO = new NacinPlacanjaDTO();
            npDTO.setId(np.getId());
            npDTO.setNaziv(np.getNaziv());

            ret.add(npDTO);
        }

        return ret;
    }

    @PostMapping(path = "/registration/{taskId}", produces = "application/json")
    public @ResponseBody String registrationSubmit(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
        HashMap<String, Object> map = this.mapListToDto(dto);

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        runtimeService.setVariable(processInstanceId, "registracionaForma", dto);
        formService.submitTaskForm(taskId, map);

        return "\""+processInstanceId+"\"";
    }

    @GetMapping(path = "/registration/validation/{processId}", produces = "application/json")
    public @ResponseBody Boolean getValidation(@PathVariable String processId) {
        return (Boolean)runtimeService.getVariable(processId, "validacija");
    }

    private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        String naucneOblasti = "";
        String naciniPlacanja = "";
        for(FormSubmissionDto temp : list){
            if(!temp.getFieldId().contains("naucnaOblast") || temp.getFieldId().contains("nacinPlacanja")) {
                map.put(temp.getFieldId(), temp.getFieldValue());
            }
            else if(temp.getFieldId().contains("naucnaOblast")){
                if(naucneOblasti.equals("")){
                    naucneOblasti+=temp.getFieldValue();
                }else{
                    naucneOblasti+=", "+temp.getFieldValue();
                }
            }
            else if(temp.getFieldId().contains("nacinPlacanja")){
                if(naciniPlacanja.equals("")){
                    naciniPlacanja+=temp.getFieldValue();
                }else{
                    naciniPlacanja+=", "+temp.getFieldValue();
                }
            }
        }
        if(!naucneOblasti.equals("")){
            map.put("naucneOblasti",naucneOblasti);
        }
        if(!naciniPlacanja.equals("")) {
            map.put("naciniPlacanja",naciniPlacanja);
        }

        return map;
    }

    @PostMapping(path = "/registration/confirmation/{processId}", produces = "application/json")
    public @ResponseBody ResponseEntity complete(@PathVariable String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).list().get(0);
        taskService.complete(task.getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('UREDNIK')")
    @GetMapping(path = "/tasks/{username}", produces = "application/json")
    public @ResponseBody List<TaskDTO> getTasksForUser(@PathVariable String username) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(username).list();
        List<TaskDTO> ret = new ArrayList<>();
        for (Task t:taskList) {

            if(t.getTaskDefinitionKey().equals("aktivacijaCasopisa")){
                boolean flag = true;

                Long id = (Long)runtimeService.getVariable(t.getProcessInstanceId(),"id");
                for (Link link:casopisRepository.getOne(id).getLinkovi()) {
                    if (link.getCompleted() == false) {
                        flag = false;
                        break;
                    }
                }
                if(flag==false){
                    continue;
                }
            }

            TaskDTO tDTO = new TaskDTO();
            tDTO.setId(t.getTaskDefinitionKey());
            tDTO.setInstanceId(t.getId());
            tDTO.setProcessId(t.getProcessInstanceId());

            TaskFormData tfd = formService.getTaskFormData(t.getId());
            List<FormField> properties = tfd.getFormFields();
            for(FormField fp : properties) {
                if (fp.getId().equals("username")) {
                    tDTO.setUser((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("glavniUrednik")) {
                    tDTO.setGlavniUrednik((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("recenzenti")) {
                    tDTO.setRecenzenti((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("urednici")) {
                    tDTO.setUrednici((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("naziv")) {
                    tDTO.setNaziv((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("clanarina")) {
                    tDTO.setClanarina((Long)fp.getValue().getValue());
                }
                if (fp.getId().equals("komeSeNaplacuje")) {
                    tDTO.setKomeSeNaplacuje((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("naciniPlacanja")) {
                    tDTO.setNaciniPlacanja((String)fp.getValue().getValue());
                }
                if (fp.getId().equals("naucneOblasti")) {
                    tDTO.setNaucneOblasti((String)fp.getValue().getValue());
                }
            }

            ret.add(tDTO);
        }

        return ret;
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/tasksLinks/{username}", produces = "application/json")
    public @ResponseBody List<TaskLinkDTO> getTasksLinks(@PathVariable String username) {
        return casopisService.getTasks(username);
    }

    @PreAuthorize("hasRole('UREDNIK')")
    @GetMapping(path = "/correction/{processId}", produces = "application/json")
    public @ResponseBody CorrectionDTO getCorrectionData(@PathVariable String processId) {

        Long casopisId = (Long)runtimeService.getVariable(processId,"id");

        return userService.getCorrectionData(casopisId);
    }

    @GetMapping(path = "/numberOfTasks/{username}", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UREDNIK')")
    public @ResponseBody Integer getNumberOfTasks(@PathVariable String username) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(username).list();
        Integer counter = 0;

        for (Task t:taskList) {
            if(t.getTaskDefinitionKey().equals("aktivacijaCasopisa") ||
                t.getTaskDefinitionKey().equals("potvrdaRecenzenta") ||
                t.getTaskDefinitionKey().equals("formaCasopisa") ||
                t.getTaskDefinitionKey().equals("unosUrednikaIRecenzenata")){

                counter++;
            }
        }

        return counter;
    }

    @PostMapping(path = "/potvrdaRecenzenta", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity potvrdaRecenzenta(@RequestBody PotvrdaRecenzentaDTO dto) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        TaskFormData tfd = formService.getTaskFormData(dto.getTaskId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            if(fp.getId().equals("recenzentOdobren")){
                map.put("recenzentOdobren",dto.getAnswer());
            }
            else if(fp.getId().equals("username")){
                map.put("username",(String)fp.getValue().getValue());
            }
        }

        formService.submitTaskForm(dto.getTaskId(),map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping(path = "/potvrdaCasopisa", produces = "application/json")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity potvrdaCasopisa(@RequestBody PotvrdaRecenzentaDTO dto) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        TaskFormData tfd = formService.getTaskFormData(dto.getTaskId());
        List<FormField> properties = tfd.getFormFields();
        for(FormField fp : properties) {
            if(fp.getId().equals("odgovorAdmina")){
                map.put("odgovorAdmina",dto.getAnswer());
            }else if(fp.getId().equals("glavniUrednik")){
                map.put("glavniUrednik",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("recenzenti")){
                map.put("recenzenti",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("urednici")){
                map.put("urednici",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("naziv")){
                map.put("naziv",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("clanarina")){
                map.put("clanarina",(Long)fp.getValue().getValue());
            }else if(fp.getId().equals("komeSeNaplacuje")){
                map.put("komeSeNaplacuje",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("naciniPlacanja")){
                map.put("naciniPlacanja",(String)fp.getValue().getValue());
            }else if(fp.getId().equals("naucneOblasti")){
                map.put("naucneOblasti",(String)fp.getValue().getValue());
            }
        }

        formService.submitTaskForm(dto.getTaskId(),map);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(path = "/uredniciRecenzenti/{processId}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody UredniciRecenzentiDTO getUrednikeRecenzente(@PathVariable String processId) {
        Long casopisId = (Long)runtimeService.getVariable(processId,"id");
        String username = (String)runtimeService.getVariable(processId,"starterIdVariable");

        UredniciRecenzentiDTO ret = userService.getUrednikRecenzent(casopisId,username);

        return ret;
    }

    @GetMapping(path = "/myPapers/{username}", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public @ResponseBody List<CasopisDTO> getMyPapers(@PathVariable String username) {
        return userService.getMyPapers(username);
    }

    @GetMapping(path = "/papers", produces = "application/json")
    public @ResponseBody List<CasopisDTO> getAllPapers() {
        return userService.getAllPapers();
    }

    @PostMapping(path = "/payments/complete/{uuid}/{nacinPlacanjaId}", produces = "application/json")
    public ResponseEntity completePayment(@PathVariable("uuid") String uuid,@PathVariable("nacinPlacanjaId") Long nacinPlacanjaId) {
        return new ResponseEntity(kpService.completePayment(uuid,nacinPlacanjaId),HttpStatus.OK);
    }

    @GetMapping(path = "/papers/{id}", produces = "application/json")
    public ResponseEntity<CasopisDTO> getPaper(@PathVariable("id") Long id){
        return new ResponseEntity<>(casopisService.getPaper(id),HttpStatus.OK);
    }

    @PostMapping(path = "/numbers", produces = "application/json")
    @PreAuthorize("hasRole('UREDNIK')")
    public ResponseEntity<Long> addNewNumber(@RequestBody IzdanjeDTO dto){
        return new ResponseEntity<Long>(kpService.addNewNumber(dto),HttpStatus.OK);
    }
}
