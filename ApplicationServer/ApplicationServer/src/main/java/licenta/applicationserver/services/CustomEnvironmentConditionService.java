package licenta.applicationserver.services;

import licenta.applicationserver.dtos.ProgramDTO;
import licenta.applicationserver.entities.CustomEnvironmentCondition;
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

    public void addCustomCondition(CustomEnvironmentCondition customCondition) {
        customRepository.save(customCondition);
    }

    public ProgramDTO updateCustomEnvironmentConditionByProgramId(Double temperature, Double humidity, Double luminosity, Integer programId){
        ProgramDTO returnProgram = new ProgramDTO();
        if(customRepository.updateCustomEnvironmentConditionByProgramId(temperature, humidity, luminosity, programId) != 0){
            CustomEnvironmentCondition tempCustomCondition = customRepository.getCustomEnvironmentConditionByProgramId(programId);

            returnProgram.setTemperature(tempCustomCondition.getTemperature());
            returnProgram.setHumidity(tempCustomCondition.getHumidity());
            returnProgram.setLuminosity(tempCustomCondition.getLuminosity());
        }

        return returnProgram;
    }

    public void deleteCustomConditionByProgramId(Integer programId) {
        customRepository.deleteByProgramId(programId);
    }

}
