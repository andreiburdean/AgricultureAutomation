package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.EnvironmentDTO;
import licenta.applicationserver.dtos.ProgramDTO;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.entities.ProgramType;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.services.EnvironmentService;
import licenta.applicationserver.services.ProgramService;
import licenta.applicationserver.services.ProgramTypeService;
import licenta.applicationserver.services.RaspberryRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/program")
public class ProgramController {

    private final ProgramService programService;
    private final EnvironmentService environmentService;
    private final ProgramTypeService programTypeService;

    @Autowired
    public ProgramController(ProgramService programService, EnvironmentService environmentService, ProgramTypeService programTypeService) {
        this.programService = programService;
        this.environmentService = environmentService;
        this.programTypeService = programTypeService;
    }

    @PostMapping("/add-program")
    public ResponseEntity<Program> addProgram(@RequestBody ProgramDTO programDTO) {

        Environment environment = environmentService.findEnvironmentById(programDTO.getEnvironmentId());
        if (environment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ProgramType programType = programTypeService.findProgramTypeById(programDTO.getProgramTypeId());
        if (programType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Program program = new Program();
        program.setProgramName(programDTO.getProgramName());
        program.setStatus(programDTO.getStatus());
        program.setEnvironment(environment);
        program.setProgramType(programType);

        Program newProgram = programService.addProgram(program);
        return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
    }

    @PostMapping("/test-send")
    public ResponseEntity<Integer> testRaspberryComSend(@RequestBody Integer value){
        RaspberryRESTService sendToRPI5 = new RaspberryRESTService();
        sendToRPI5.sendDummyData(value);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/receive-sensor-data")
    public ResponseEntity<Integer> testRaspberryComRec(@RequestBody Object value){
        System.out.println(value);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}