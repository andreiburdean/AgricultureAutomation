package licenta.applicationserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.repositories.ProgramRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Optional<Program> findProgramById(Integer programId){
        return programRepository.findByProgramId(programId);
    }

    public ResponseEntity<List<Program>> findProgramsByEnvironmentId(Integer environmentId){

        Optional<List<Program>> programs = programRepository.findProgramsByEnvironmentId(environmentId);

        if(programs.isPresent() && !programs.get().isEmpty()){
            return ResponseEntity.ok(programs.get());
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    public Program addProgram(Program program) {
        return programRepository.save(program);
    }

    public void deleteProgram(Integer programId) {
        programRepository.deleteById(programId);
    }

    public Integer stopPrograms(Integer environmentId){
        return programRepository.stopPrograms(environmentId);
    }

    public Integer startProgram(Integer programId){
        return programRepository.startProgram(programId);
    }

    public Integer stopProgram(Integer programId){
        return programRepository.stopProgram(programId);
    }
}
