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

        ProgramType programType = programTypeService.findProgramTypeById(programDTO.getProgramTypeId());
        if (programType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Program program = new Program();
        program.setProgramName(programDTO.getProgramName());
        program.setStatus(0);
        program.setEnvironment(environment);
        program.setProgramType(programType);

        Program newProgram = programService.addProgram(program);
        return new ResponseEntity<>(newProgram, HttpStatus.CREATED);
    }

    @GetMapping("{environmentId}/get-programs")
    public ResponseEntity<List<Program>> getProgramsByEnvironmentId(@PathVariable Integer environmentId) {
        return programService.findProgramsByEnvironmentId(environmentId);
    }

    @DeleteMapping("{programId}/delete-program")
    public ResponseEntity<Void> deleteProgram(@PathVariable Integer programId) {
        if (programService.findProgramById(programId).isPresent()) {
            programService.deleteProgram(programId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("{environmentId}/{programId}/start-program")
    public ResponseEntity<Boolean> startProgram(@PathVariable Integer environmentId, @PathVariable Integer programId) {
        if(programService.stopPrograms(environmentId) != 0){
            if(programService.startProgram(programId) != 0){
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("{programId}/stop-program")
    public ResponseEntity<Boolean> stopProgram(@PathVariable Integer programId) {
        if(programService.stopProgram(programId) != 0){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}