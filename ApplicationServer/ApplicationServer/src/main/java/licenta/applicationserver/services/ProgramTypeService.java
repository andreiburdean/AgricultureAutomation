package licenta.applicationserver.services;

import licenta.applicationserver.entities.ProgramType;
import licenta.applicationserver.repositories.ProgramTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProgramTypeService {

    private final ProgramTypeRepository programTypeRepository;

    @Autowired
    public ProgramTypeService(ProgramTypeRepository programTypeRepository) {
        this.programTypeRepository = programTypeRepository;
    }
    public ProgramType findProgramTypeById(Integer programTypeId) {
        Optional<ProgramType> optionalProgramType = programTypeRepository.findById(programTypeId);
        return optionalProgramType.orElse(null);
    }
}
