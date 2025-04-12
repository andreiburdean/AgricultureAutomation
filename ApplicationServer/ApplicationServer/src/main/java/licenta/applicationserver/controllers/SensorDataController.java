package licenta.applicationserver.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/api/sensor-data")
public class SensorDataController {

    Map<String, Double> sensorDataMap = new HashMap<>();
    {
        sensorDataMap.put("temperature", 0.0);
        sensorDataMap.put("humidity", 0.0);
        sensorDataMap.put("pressure", 0.0);
        sensorDataMap.put("luminosity", 0.0);
        sensorDataMap.put("soilMoisture", 0.0);
    }

    @PostMapping("/receive-sensor-data")
    public ResponseEntity<Integer> receiveSensorData(@RequestBody Map<String, Double> sensorData) {
        System.out.println("Received sensor data:" + sensorData);
        sensorDataMap.put("temperature", sensorData.get("temperature"));
        sensorDataMap.put("humidity", sensorData.get("humidity"));
        sensorDataMap.put("pressure", sensorData.get("pressure"));
        sensorDataMap.put("luminosity", sensorData.get("luminosity"));
        sensorDataMap.put("soilMoisture", sensorData.get("soilMoisture"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{environment_id}/get-sensor-data")
    public ResponseEntity<Map<String, Double>> getSensorData(@PathVariable Integer environment_id) {
        return ResponseEntity.ok(this.sensorDataMap);
    }
}

