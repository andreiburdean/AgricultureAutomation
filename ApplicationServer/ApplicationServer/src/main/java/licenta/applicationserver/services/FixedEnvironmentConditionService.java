package licenta.applicationserver.services;

import licenta.applicationserver.repositories.FixedEnvironmentConditionRepository;
import org.springframework.stereotype.Service;

@Service
public class FixedEnvironmentConditionService {
    private final FixedEnvironmentConditionRepository fixedRepository;

    public FixedEnvironmentConditionService(FixedEnvironmentConditionRepository fixedRepository) {
        this.fixedRepository = fixedRepository;
    }
}
