package licenta.applicationserver.services;

import licenta.applicationserver.dtos.ControlDTO;
import licenta.applicationserver.entities.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.repositories.EnvironmentRepository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final ControlStatusService controlStatusService;
    private final ProgramService programService;
    private final CustomEnvironmentConditionService customEnvironmentConditionService;
    private final SensorDataService sensorDataService;

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepository, ControlStatusService controlStatusService, ProgramService programService, CustomEnvironmentConditionService customEnvironmentConditionService, SensorDataService sensorDataService) {
        this.environmentRepository = environmentRepository;
        this.controlStatusService = controlStatusService;
        this.programService = programService;
        this.customEnvironmentConditionService = customEnvironmentConditionService;
        this.sensorDataService = sensorDataService;
    }

    public Environment addEnvironment(Environment environment) {
        return environmentRepository.save(environment);
    }

    public Environment findEnvironmentById(Integer environmentId) {
        Optional<Environment> optionalEnvironment = environmentRepository.findById(environmentId);
        return optionalEnvironment.orElse(null);
    }

    public Environment findEnvironmentByRaspberryId(Integer raspberryId) {
        Optional<Environment> optionalEnvironment = environmentRepository.findEnvironmentByRaspberryId(raspberryId);
        return optionalEnvironment.orElse(null);
    }

    public ResponseEntity<List<Environment>> findEnvironmentsByUserId(Integer userId){
        Optional<List<Environment>> environments = environmentRepository.findEnvironmentsByUserId(userId);

        if (environments.isPresent() && !environments.get().isEmpty()) {
            return ResponseEntity.ok(environments.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    public void deleteEnvironment(Integer environmentId) {
        Optional<List<Program>> tempList =  programService.findProgramsByEnvironmentIdForDelete(environmentId);
        List<Program> programList;
        if(tempList.isPresent()){
            programList = tempList.get();
            for (Program program : programList){
                customEnvironmentConditionService.deleteCustomConditionByProgramId(program.getProgramId());
            }
        }

        programService.deleteProgramsByEnvironmentId(environmentId);
        sensorDataService.deleteSensorDataByEnvironmentId(environmentId);
        controlStatusService.deleteControlStatusByEnvironmentId(environmentId);
        environmentRepository.deleteById(environmentId);
    }

    @Async
    public void sendControlPostToRPi5(ControlDTO controlDTO, Integer raspberryId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ControlDTO> requestEntity = new HttpEntity<>(controlDTO, headers);
            String rpiServerUrl = "http://192.168.108.171:5000/" + raspberryId + "/receive-control-command";

            ResponseEntity<String> rpiResponse = restTemplate.postForEntity(rpiServerUrl, requestEntity, String.class);
            System.out.println("Response from RPi5: " + rpiResponse.getBody());
        } catch (Exception e) {
            System.err.println("Error sending POST request to RPi5 server: " + e.getMessage());
        }
    }
}
