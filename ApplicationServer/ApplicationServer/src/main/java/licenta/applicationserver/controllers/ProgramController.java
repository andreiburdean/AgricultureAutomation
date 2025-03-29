package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.ProgramDTO;
import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.entities.ProgramType;
import licenta.applicationserver.services.EnvironmentService;
import licenta.applicationserver.services.ProgramService;
import licenta.applicationserver.services.ProgramTypeService;
import licenta.applicationserver.services.RaspberryRESTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("{environmentId}/add-program")
    public ResponseEntity<Program> addProgram(@PathVariable Integer environmentId, @RequestBody ProgramDTO programDTO) {

        Environment environment = environmentService.findEnvironmentById(environmentId);
        if (environment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println("CEVA");

        ProgramType programType = programTypeService.findProgramTypeById(programDTO.getProgramTypeId());
        if (programType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println("CEVA2");

        Program program = new Program();
        program.setProgramName(programDTO.getProgramName());
        program.setStatus(programDTO.getStatus());
        program.setEnvironment(environment);
        program.setProgramType(programType);

        Program newProgram = programService.addProgram(program);
        System.out.println("CEVA3");
        return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
    }

    @GetMapping("{environmentId}/get-programs/")
    public ResponseEntity<List<Program>> getProgramsByEnvironmentId(@PathVariable Integer environmentId) {
        return programService.findProgramsByEnvironmentId(environmentId);
    }
}