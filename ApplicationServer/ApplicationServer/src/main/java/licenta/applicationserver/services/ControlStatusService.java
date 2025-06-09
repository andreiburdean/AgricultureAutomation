package licenta.applicationserver.services;

import licenta.applicationserver.entities.ControlStatus;
import licenta.applicationserver.entities.SensorData;
import licenta.applicationserver.repositories.ControlStatusRepository;
import licenta.applicationserver.repositories.SensorDataRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ControlStatusService {
    private final ControlStatusRepository controlStatusRepository;

    public ControlStatusService(ControlStatusRepository controlStatusRepository) {
        this.controlStatusRepository = controlStatusRepository;
    }

    public ControlStatus updateControlStatus(Integer environmentId,
                                             Integer switchControl,
                                             Integer fan,
                                             Integer pump,
                                             Integer led){
        ControlStatus returnControlStatus = new ControlStatus();

        if(controlStatusRepository.updateControlStatus(environmentId, switchControl, fan, pump, led) != 0){
            Optional<ControlStatus> tempControlStatus = getControlStatusByEnvironmentId(environmentId);

            if(tempControlStatus.isPresent()){
                returnControlStatus.setSwitchControl(tempControlStatus.get().getSwitchControl());
                returnControlStatus.setFan(tempControlStatus.get().getFan());
                returnControlStatus.setPump(tempControlStatus.get().getPump());
                returnControlStatus.setLed(tempControlStatus.get().getLed());
            }
        }
        return returnControlStatus;
    }

    public ControlStatus addControlStatus(ControlStatus controlStatus){
        return controlStatusRepository.save(controlStatus);
    }

    public void deleteControlStatusByEnvironmentId(Integer environmentId) {
        controlStatusRepository.deleteByEnvironmentId(environmentId);
    }

    public Optional<ControlStatus> getControlStatusByEnvironmentId(Integer environmentId){
        return controlStatusRepository.findControlStatusByEnvironmentId(environmentId);
    }
}
