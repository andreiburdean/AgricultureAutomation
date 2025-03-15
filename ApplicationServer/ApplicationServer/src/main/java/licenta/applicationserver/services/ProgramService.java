package licenta.applicationserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.repositories.ProgramRepository;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public Program addProgram(Program program) {
        return programRepository.save(program);
    }

    public String deleteProgram(Program program) {
        Program existingProgram = programRepository.findByProgramId(program.getProgramId()).orElse(null);

        if (existingProgram != null) {
            programRepository.deleteById(existingProgram.getProgramId());
            return "Program deleted successfully.";
        }
        return "Program not found.";
    }
}
