package licenta.applicationserver.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import licenta.applicationserver.dtos.ConditionsDTO;
import licenta.applicationserver.entities.CustomEnvironmentCondition;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.entities.FixedEnvironmentCondition;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.repositories.CustomEnvironmentConditionRepository;
import licenta.applicationserver.repositories.EnvironmentRepository;
import licenta.applicationserver.repositories.FixedEnvironmentConditionRepository;
import licenta.applicationserver.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class RaspberryRESTService {

    private final EnvironmentRepository environmentRepository;
    private final ProgramRepository programRepository;
    private final CustomEnvironmentConditionRepository customRepository;
    private final FixedEnvironmentConditionRepository fixedRepository;

    @Autowired
    public RaspberryRESTService(ProgramRepository programRepository, EnvironmentRepository environmentRepository, CustomEnvironmentConditionRepository customRepository, FixedEnvironmentConditionRepository fixedRepository) {
        this.programRepository = programRepository;
        this.environmentRepository = environmentRepository;
        this.customRepository = customRepository;
        this.fixedRepository = fixedRepository;
    }

    public Optional<Environment> getEnvironmentByRaspberryId(Integer raspberryId){
        return environmentRepository.findEnvironmentByRaspberryId(raspberryId);
    }

    public Optional<Program> getActiveProgramByEnvironmentId(Integer environmentId){
        return programRepository.findActiveProgramByEnvironmentId(environmentId);
    }

    public CustomEnvironmentCondition getCustomEnvironmentConditionByProgramId(Integer programId){
        return customRepository.getCustomEnvironmentConditionByProgramId(programId);
    }

    public FixedEnvironmentCondition getFixedEnvironmentConditionByProgramTypeId(Integer programTypeId){
        return fixedRepository.getFixedEnvironmentConditionByProgramTypeId(programTypeId);
    }

    @Async
    public void sendStartPostToRPi5(ConditionsDTO conditionsDTO) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ConditionsDTO> requestEntity = new HttpEntity<>(conditionsDTO, headers);
            String rpiServerUrl = "http://192.168.100.137:5000/receive-program-start";

            ResponseEntity<String> rpiResponse = restTemplate.postForEntity(rpiServerUrl, requestEntity, String.class);
            System.out.println("Response from RPi5: " + rpiResponse.getBody());
        } catch (Exception e) {
            System.err.println("Error sending POST request to RPi5 server: " + e.getMessage());
        }
    }

    @Async
    public void sendStopPostToRPi5() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            String payload = objectMapper.writeValueAsString(0);
            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

            String rpiServerUrl = "http://192.168.100.137:5000/receive-program-stop";
            ResponseEntity<String> rpiResponse = restTemplate.postForEntity(rpiServerUrl, requestEntity, String.class);
            System.out.println("Response from RPi5: " + rpiResponse.getBody());
        } catch (Exception e) {
            System.err.println("Error sending POST request to RPi5 server: " + e.getMessage());
        }
    }
}
