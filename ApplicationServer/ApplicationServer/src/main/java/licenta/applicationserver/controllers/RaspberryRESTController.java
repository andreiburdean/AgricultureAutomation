package licenta.applicationserver.controllers;


import licenta.applicationserver.dtos.ConditionsDTO;
import licenta.applicationserver.dtos.SensorDataDTO;
import licenta.applicationserver.entities.*;
import licenta.applicationserver.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RestController
@RequestMapping("/api/rpi")
public class RaspberryRESTController {


    private final RaspberryRESTService rpiService;
    private final SensorDataService sensorDataService;
    private final EnvironmentService environmentService;

    @Autowired
    public RaspberryRESTController(RaspberryRESTService rpiService, SensorDataService sensorDataService, EnvironmentService environmentService) {
        this.rpiService = rpiService;
        this.sensorDataService = sensorDataService;
        this.environmentService = environmentService;
    }

    @PostMapping("/{raspberryId}/receive-sensor-data")
    public ResponseEntity<Integer> receiveSensorData(@PathVariable Integer raspberryId, @RequestBody Map<String, Double> sensorData) {
        Environment environment = environmentService.findEnvironmentByRaspberryId(raspberryId);
        Integer environmentId = environment.getEnvironmentId();
        Map<String, Double> sensorDataMap = new HashMap<>();
        {
            sensorDataMap.put("temperature", 0.0);
            sensorDataMap.put("humidity", 0.0);
            sensorDataMap.put("pressure", 0.0);
            sensorDataMap.put("luminosity", 0.0);
            sensorDataMap.put("soilMoisture", 0.0);
        }

        System.out.println("Received sensor data:" + sensorData);
        sensorDataMap.put("temperature", sensorData.get("temperature"));
        sensorDataMap.put("humidity", sensorData.get("humidity"));
        sensorDataMap.put("pressure", sensorData.get("pressure"));
        sensorDataMap.put("luminosity", sensorData.get("luminosity"));
        sensorDataMap.put("soilMoisture", sensorData.get("soilMoisture"));

        if(sensorDataService.getSensorDataByEnvironmentId(environmentId).isPresent()){
            sensorDataService.updateSensorData(environmentId,
                    sensorDataMap.get("temperature"),
                    sensorDataMap.get("humidity"),
                    sensorDataMap.get("luminosity"),
                    sensorDataMap.get("pressure"),
                    sensorDataMap.get("soilMoisture"));
        }
        else{
            SensorData tempSensorData = new SensorData();
            tempSensorData.setEnvironment(environment);
            tempSensorData.setTemperature(sensorDataMap.get("temperature"));
            tempSensorData.setHumidity(sensorDataMap.get("humidity"));
            tempSensorData.setLuminosity(sensorDataMap.get("luminosity"));
            tempSensorData.setPressure(sensorDataMap.get("pressure"));
            tempSensorData.setSoilMoisture(sensorDataMap.get("soilMoisture"));
            sensorDataService.addSensorData(tempSensorData);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{environmentId}/get-sensor-data")
    public ResponseEntity<SensorDataDTO> getSensorData(@PathVariable Integer environmentId){
        Optional<SensorData> sensorData = sensorDataService.getSensorDataByEnvironmentId(environmentId);
        if(sensorData.isPresent()){
            SensorDataDTO returnSensorData = new SensorDataDTO();
            returnSensorData.setTemperature(sensorData.get().getTemperature());
            returnSensorData.setHumidity(sensorData.get().getHumidity());
            returnSensorData.setLuminosity(sensorData.get().getLuminosity());
            returnSensorData.setPressure(sensorData.get().getPressure());
            returnSensorData.setSoilMoisture(sensorData.get().getSoilMoisture());
            return new ResponseEntity<>(returnSensorData, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/{raspberryId}/on-startup")
    public ResponseEntity<Object> provideStartupData(@PathVariable Integer raspberryId){
        System.out.println("apel");
        Optional<Environment> environment = rpiService.getEnvironmentByRaspberryId(raspberryId);

        if(environment.isPresent()){
            Optional<Program> program = rpiService.getActiveProgramByEnvironmentId(environment.get().getEnvironmentId());

            if(program.isPresent()){
                if(program.get().getProgramType().getProgramTypeId() != 5){
                    FixedEnvironmentCondition fixedEnvironment = rpiService.getFixedEnvironmentConditionByProgramTypeId(program.get().getProgramType().getProgramTypeId());
                    ConditionsDTO conditionsDTO = new ConditionsDTO(
                            fixedEnvironment.getTemperature(),
                            fixedEnvironment.getHumidity(),
                            fixedEnvironment.getLuminosity());

                    return new ResponseEntity<>(conditionsDTO, HttpStatus.OK);
                }
                else{
                    CustomEnvironmentCondition customEnvironment = rpiService.getCustomEnvironmentConditionByProgramId(program.get().getProgramId());
                    ConditionsDTO conditionsDTO = new ConditionsDTO(
                            customEnvironment.getTemperature(),
                            customEnvironment.getHumidity(),
                            customEnvironment.getLuminosity());

                    return new ResponseEntity<>(conditionsDTO, HttpStatus.OK);
                }
            }
            else{
                return ResponseEntity.ok().body(Collections.emptyMap());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

