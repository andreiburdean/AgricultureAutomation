package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.ControlDTO;
import licenta.applicationserver.dtos.EnvironmentDTO;
import licenta.applicationserver.entities.ControlStatus;
import licenta.applicationserver.entities.SensorData;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.services.ControlStatusService;
import licenta.applicationserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.services.EnvironmentService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RestController
@RequestMapping("/api/environment")
public class EnvironmentController {

    private final EnvironmentService environmentService;
    private final ControlStatusService controlStatusService;
    private final UserService userService;

    @Autowired
    public EnvironmentController(EnvironmentService environmentService, ControlStatusService controlStatusService, UserService userService) {
        this.environmentService = environmentService;
        this.controlStatusService = controlStatusService;
        this.userService = userService;
    }

    @PostMapping("{userId}/add-environment")
    public ResponseEntity<EnvironmentDTO> addEnvironment(@PathVariable Integer userId, @RequestBody EnvironmentDTO environmentDTO) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Environment env = environmentService.findEnvironmentByRaspberryId(environmentDTO.getRaspberryId());
        Integer raspberryId;

        if (env != null) {
            raspberryId = env.getRaspberryId();
            if (Objects.equals(raspberryId, environmentDTO.getRaspberryId())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        // If env is null or raspberryId does not match, create a new environment
        Environment environment = new Environment();
        environment.setRaspberryId(environmentDTO.getRaspberryId());
        environment.setRaspberryIP(environmentDTO.getRaspberryIp());
        environment.setEnvironmentName(environmentDTO.getEnvironmentName());
        environment.setUser(user);

        Environment newEnvironment = environmentService.addEnvironment(environment);
        EnvironmentDTO newEnvironmentDTO = new EnvironmentDTO(newEnvironment.getEnvironmentId(), newEnvironment.getRaspberryId(), newEnvironment.getRaspberryIP(), newEnvironment.getEnvironmentName());
        System.out.println("New environment added by user: " + user.getEmail());
        return new ResponseEntity<>(newEnvironmentDTO, HttpStatus.CREATED);
    }


    @GetMapping("{userId}/get-environments")
    public ResponseEntity<List<Environment>> getEnvironmentsByUserId(@PathVariable Integer userId) {
        return environmentService.findEnvironmentsByUserId(userId);
    }

    @DeleteMapping("{userId}/delete-environment/{environmentId}")
    public ResponseEntity<Void> deleteEnvironment(@PathVariable Integer environmentId) {
        if (environmentService.findEnvironmentById(environmentId) != null) {
            environmentService.deleteEnvironment(environmentId);
            System.out.println("User deleted the environment with id: " + environmentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{environmentId}/control-environment")
    public void controlEnvironment(@PathVariable Integer environmentId, @RequestBody ControlDTO controlDTO) {

        if(controlStatusService.getControlStatusByEnvironmentId(environmentId).isPresent()){
            controlStatusService.updateControlStatus(environmentId,
                    controlDTO.getSwitchControl(),
                    controlDTO.getFan(),
                    controlDTO.getPump(),
                    controlDTO.getLed());

            Environment environment = environmentService.findEnvironmentById(environmentId);
            Integer raspberryId = environment.getRaspberryId();
            environmentService.sendControlPostToRPi5(controlDTO, raspberryId);
        }
        else{
            Environment environment = environmentService.findEnvironmentById(environmentId);
            Integer raspberryId = environment.getRaspberryId();

            ControlStatus tempControlStatus = new ControlStatus();
            tempControlStatus.setEnvironment(environment);
            tempControlStatus.setSwitchControl(controlDTO.getSwitchControl());
            tempControlStatus.setFan(controlDTO.getFan());
            tempControlStatus.setPump(controlDTO.getPump());
            tempControlStatus.setLed(controlDTO.getLed());
            controlStatusService.addControlStatus(tempControlStatus);

            environmentService.sendControlPostToRPi5(controlDTO, raspberryId);
        }
    }

    @GetMapping("/{environmentId}/get-control-status")
    public ResponseEntity<ControlDTO> getControlStatus(@PathVariable Integer environmentId) {
        Optional<ControlStatus> controlStatus = controlStatusService.getControlStatusByEnvironmentId(environmentId);

        if (controlStatus.isPresent()) {
            ControlDTO controlDTO = new ControlDTO(controlStatus.get().getSwitchControl(),
                    controlStatus.get().getFan(),
                    controlStatus.get().getPump(),
                    controlStatus.get().getLed());
            return ResponseEntity.ok(controlDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}