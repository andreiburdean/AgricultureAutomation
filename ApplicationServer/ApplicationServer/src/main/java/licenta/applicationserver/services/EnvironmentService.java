package licenta.applicationserver.services;

import licenta.applicationserver.entities.User;
import licenta.applicationserver.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.repositories.EnvironmentRepository;

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
}
