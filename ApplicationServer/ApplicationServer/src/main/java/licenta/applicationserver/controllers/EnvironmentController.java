package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.AccessRequest;
import licenta.applicationserver.dtos.EnvironmentDTO;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.services.EnvironmentService;

@Controller
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/environment")
public class EnvironmentController {

    private final EnvironmentService environmentService;
    private final UserService userService;

    @Autowired
    public EnvironmentController(EnvironmentService environmentService, UserService userService) {
        this.environmentService = environmentService;
        this.userService = userService;
    }

    @PostMapping("/add-environment")
    public ResponseEntity<Environment> addEnvironment(@RequestBody EnvironmentDTO environmentDTO) {

        User user = userService.findUserById(environmentDTO.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Environment environment = new Environment();
        environment.setRaspberryId(environmentDTO.getRaspberryId());
        environment.setRaspberryIP(environmentDTO.getRaspberryIp());
        environment.setAccessCode(environmentDTO.getAccessCode());
        environment.setUser(user);

        Environment newEnvironment = environmentService.addEnvironment(environment);
        return new ResponseEntity<>(newEnvironment, HttpStatus.CREATED);
    }

    @PostMapping("/access-environment")
    public ResponseEntity<String> accessEnvironment(@RequestBody AccessRequest accessRequest) {
        return environmentService.accessEnvironment(accessRequest.getRaspberryId(), accessRequest.getAccessCode());
    }
}