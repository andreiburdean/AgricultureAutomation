package licenta.applicationserver.services;

import licenta.applicationserver.dtos.ConditionsDTO;
import licenta.applicationserver.dtos.ControlDTO;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.security.PasswordHasher;
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

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
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
        environmentRepository.deleteById(environmentId);
    }

    @Async
    public void sendControlPostToRPi5(ControlDTO controlDTO, Integer raspberryId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ControlDTO> requestEntity = new HttpEntity<>(controlDTO, headers);
            String rpiServerUrl = "http://192.168.100.137:5000/" + raspberryId + "/receive-control-command";

            ResponseEntity<String> rpiResponse = restTemplate.postForEntity(rpiServerUrl, requestEntity, String.class);
            System.out.println("Response from RPi5: " + rpiResponse.getBody());
        } catch (Exception e) {
            System.err.println("Error sending POST request to RPi5 server: " + e.getMessage());
        }
    }
}
