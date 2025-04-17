package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.ConditionsDTO;
import licenta.applicationserver.dtos.ProgramDTO;
import licenta.applicationserver.entities.*;
import licenta.applicationserver.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService programService;
    private final CustomEnvironmentConditionService customConditionService;
    private final EnvironmentService environmentService;
    private final RaspberryRESTService rpiService;
    private final ProgramTypeService programTypeService;

    @Autowired
    public ProgramController(ProgramService programService, CustomEnvironmentConditionService customConditionService, EnvironmentService environmentService, RaspberryRESTService rpiService, ProgramTypeService programTypeService) {
        this.programService = programService;
        this.customConditionService = customConditionService;
        this.environmentService = environmentService;
        this.rpiService = rpiService;
        this.programTypeService = programTypeService;
    }

    @PostMapping("{environmentId}/add-program")
    public ResponseEntity<Program> addProgram(@PathVariable Integer environmentId, @RequestBody ProgramDTO programDTO) {

        Environment environment = environmentService.findEnvironmentById(environmentId);
        if (environment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProgramType programType = programTypeService.findProgramTypeById(programDTO.getProgramTypeId());
        if (programType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if(programType.getProgramTypeId() != 5){

            Program program = new Program();
            program.setProgramName(programDTO.getProgramName());
            program.setStatus(0);
            program.setEnvironment(environment);
            program.setProgramType(programType);

            Program newProgram = programService.addProgram(program);
            return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
        }else{
            Program program = new Program();
            program.setProgramName(programDTO.getProgramName());
            program.setStatus(0);
            program.setEnvironment(environment);
            program.setProgramType(programType);
            Program newProgram = programService.addProgram(program);

            CustomEnvironmentCondition customCondition = new CustomEnvironmentCondition();
            customCondition.setProgram(newProgram);
            customCondition.setProgramType(programType);
            customCondition.setTemperature(programDTO.getTemperature());
            customCondition.setHumidity(programDTO.getHumidity());
            customCondition.setLuminosity(programDTO.getLuminosity());

            customConditionService.addCustomCondition(customCondition);
            return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
        }
    }

    @GetMapping("{environmentId}/get-programs")
    public ResponseEntity<List<ProgramDTO>> getProgramsByEnvironmentId(@PathVariable Integer environmentId) {
        return programService.findProgramsByEnvironmentId(environmentId);
    }

    @PutMapping("{programId}/update-program")
    public ResponseEntity<ProgramDTO> updateCustomEnvironmentConditionByProgramId(@PathVariable Integer programId, @RequestBody ProgramDTO programDTO){
        ProgramDTO responseEntity = customConditionService.updateCustomEnvironmentConditionByProgramId(programDTO.getTemperature(), programDTO.getHumidity(), programDTO.getLuminosity(), programId);

        if (responseEntity != null) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                ObjectMapper objectMapper = new ObjectMapper();
                String payload = objectMapper.writeValueAsString(responseEntity);
                HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

                String rpiServerUrl = "http://192.168.100.137:5000/receive-custom-program";
                ResponseEntity<String> rpiResponse = restTemplate.postForEntity(rpiServerUrl, requestEntity, String.class);
                System.out.println("Response from RPi5: " + rpiResponse.getBody());
            } catch (Exception e) {
                System.err.println("Error sending POST request to RPi5 server: " + e.getMessage());
            }
        }
        assert responseEntity != null;
        return new ResponseEntity<>(responseEntity, HttpStatus.OK);
    }

    @DeleteMapping("{programId}/delete-program")
    public ResponseEntity<Void> deleteProgram(@PathVariable Integer programId) {
        if (programService.findProgramById(programId).isPresent()) {
            Program program = programService.findProgramById(programId).get();
            if(program.getProgramType().getProgramTypeId() == 5){
                customConditionService.deleteCustomConditionByProgramId(programId);
            }
            programService.deleteProgram(programId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{environmentId}/{programId}/start-program")
    public ResponseEntity<Boolean> startProgram(@PathVariable Integer environmentId, @PathVariable Integer programId) {
        System.out.println("start program");

        boolean dbResult = false;
        if(programService.stopPrograms(environmentId) != 0) {
            if(programService.startProgram(programId) != 0) {
                dbResult = true;
            }
        }

        if (dbResult) {
            Optional<Program> program = programService.findProgramById(programId);
            if(program.isPresent()){
                if(program.get().getProgramType().getProgramTypeId() != 5){
                    FixedEnvironmentCondition fixedEnvironment = rpiService.getFixedEnvironmentConditionByProgramTypeId(program.get().getProgramType().getProgramTypeId());
                    ConditionsDTO conditionsDTO = new ConditionsDTO(
                            fixedEnvironment.getTemperature(),
                            fixedEnvironment.getHumidity(),
                            fixedEnvironment.getLuminosity());

                    rpiService.sendStartPostToRPi5(conditionsDTO);
                }
                else{
                    CustomEnvironmentCondition customEnvironment = rpiService.getCustomEnvironmentConditionByProgramId(program.get().getProgramId());
                    ConditionsDTO conditionsDTO = new ConditionsDTO(
                            customEnvironment.getTemperature(),
                            customEnvironment.getHumidity(),
                            customEnvironment.getLuminosity());

                    rpiService.sendStartPostToRPi5(conditionsDTO);
                }
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{programId}/stop-program")
    public ResponseEntity<Boolean> stopProgram(@PathVariable Integer programId) {
        System.out.println("stop program");
        Integer result = programService.stopProgram(programId);
        if(result != 0) {
            try {
                rpiService.sendStopPostToRPi5();
            } catch(Exception e) {
                System.err.println("Error calling RPi5Service: " + e.getMessage());
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}