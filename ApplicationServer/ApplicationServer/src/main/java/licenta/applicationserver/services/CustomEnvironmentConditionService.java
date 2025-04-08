package licenta.applicationserver.services;

import licenta.applicationserver.entities.CustomEnvironmentCondition;
import licenta.applicationserver.entities.Program;
import licenta.applicationserver.repositories.CustomEnvironmentConditionRepository;
import licenta.applicationserver.repositories.CustomEnvironmentConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomEnvironmentConditionService {
    private final CustomEnvironmentConditionRepository customRepository;

    @Autowired
    public CustomEnvironmentConditionService(CustomEnvironmentConditionRepository customRepository) {
        this.customRepository = customRepository;
    }

    public CustomEnvironmentCondition addCustomCondition(CustomEnvironmentCondition customCondition) {
        return customRepository.save(customCondition);
    }

    public void deleteCustomConditionByProgramId(Integer programId) {
        customRepository.deleteByProgramId(programId);
    }

}
