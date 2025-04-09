package licenta.applicationserver.services;

import licenta.applicationserver.dtos.ProgramDTO;
import licenta.applicationserver.entities.CustomEnvironmentCondition;
import licenta.applicationserver.repositories.CustomEnvironmentConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.repositories.ProgramRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.collections.ArrayHelper.forEach;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;
    private final CustomEnvironmentConditionRepository customRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository, CustomEnvironmentConditionRepository customRepository) {
        this.programRepository = programRepository;
        this.customRepository = customRepository;
    }

    public Optional<Program> findProgramById(Integer programId){
        return programRepository.findByProgramId(programId);
    }

    public ResponseEntity<List<ProgramDTO>> findProgramsByEnvironmentId(Integer environmentId){

        Optional<List<Program>> programs = programRepository.findProgramsByEnvironmentId(environmentId);
        List<Program> programList = programs.orElseThrow(() -> new RuntimeException("No programs found"));
        List<ProgramDTO> returnList = new ArrayList<>();

        for(Program program : programList){
            ProgramDTO tempProgramDTO = new ProgramDTO();

            if(program.getProgramType().getProgramTypeId() != 5){
                tempProgramDTO.setProgramId(program.getProgramId());
                tempProgramDTO.setProgramTypeId(program.getProgramType().getProgramTypeId());
                tempProgramDTO.setProgramName(program.getProgramName());
                tempProgramDTO.setStatus(program.getStatus());

                returnList.add(tempProgramDTO);
            }
            else{
                CustomEnvironmentCondition tempCustomCondition;
                tempCustomCondition = customRepository.getCustomEnvironmentConditionByProgramId(program.getProgramId());

                tempProgramDTO.setProgramId(program.getProgramId());
                tempProgramDTO.setProgramTypeId(program.getProgramType().getProgramTypeId());
                tempProgramDTO.setProgramName(program.getProgramName());
                tempProgramDTO.setStatus(program.getStatus());

                tempProgramDTO.setTemperature(tempCustomCondition.getTemperature());
                tempProgramDTO.setHumidity(tempCustomCondition.getHumidity());
                tempProgramDTO.setLuminosity(tempCustomCondition.getLuminosity());

                returnList.add(tempProgramDTO);
            }
        }

        if(!returnList.isEmpty()){
            return ResponseEntity.ok(returnList);
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
