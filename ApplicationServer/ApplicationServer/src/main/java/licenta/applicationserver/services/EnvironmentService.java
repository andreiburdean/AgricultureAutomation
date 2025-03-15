package licenta.applicationserver.services;

import licenta.applicationserver.entities.User;
import licenta.applicationserver.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.repositories.EnvironmentRepository;

import java.util.Optional;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    @Autowired
    public EnvironmentService(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    public Environment addEnvironment(Environment environment) {
        String hashedAccessCode = PasswordHasher.hashPassword(environment.getAccessCode());
        environment.setAccessCode(hashedAccessCode);
        return environmentRepository.save(environment);
    }

    public Environment findEnvironmentById(Integer environmentId) {
        Optional<Environment> optionalEnvironment = environmentRepository.findById(environmentId);
        return optionalEnvironment.orElse(null);
    }

    public ResponseEntity<String> accessEnvironment(Integer raspberryId, String accessCode){
        Optional<Environment> optionalEnvironment = environmentRepository.findByRaspberryId(raspberryId);

        if (optionalEnvironment.isPresent()) {
            Environment environment = optionalEnvironment.get();
            String hashedAccessCode = PasswordHasher.hashPassword(accessCode);
            if (environment.getAccessCode().equals(hashedAccessCode)) {
                return ResponseEntity.ok("Authorized");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Environment not found");
        }
    }
}
