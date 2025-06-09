package licenta.applicationserver.services;

import licenta.applicationserver.entities.SensorData;
import licenta.applicationserver.repositories.SensorDataRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SensorDataService {
    private final SensorDataRepository sensorDataRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository){
        this.sensorDataRepository = sensorDataRepository;
    }

    public SensorData updateSensorData(Integer environmentId,
                                       Double temperature,
                                       Double humidity,
                                       Double luminosity,
                                       Double pressure,
                                       Double soilMositure){
        SensorData returnSensorData = new SensorData();

        if(sensorDataRepository.updateSensorData(environmentId, temperature, humidity, luminosity, pressure, soilMositure) != 0){
            Optional<SensorData> tempSensorData = getSensorDataByEnvironmentId(environmentId);

            if(tempSensorData.isPresent()){
                returnSensorData.setTemperature(tempSensorData.get().getTemperature());
                returnSensorData.setHumidity(tempSensorData.get().getHumidity());
                returnSensorData.setLuminosity(tempSensorData.get().getLuminosity());
                returnSensorData.setPressure(tempSensorData.get().getPressure());
                returnSensorData.setSoilMoisture(tempSensorData.get().getSoilMoisture());
            }
        }
        return returnSensorData;
    }

    public SensorData addSensorData(SensorData sensorData){
        return sensorDataRepository.save(sensorData);
    }

    public void deleteSensorDataByEnvironmentId(Integer environmentId) {
        sensorDataRepository.deleteByEnvironmentId(environmentId);
    }
    public Optional<SensorData> getSensorDataByEnvironmentId(Integer environmentId){
       return sensorDataRepository.findSensorDataByEnvironmentId(environmentId);
    }
}
