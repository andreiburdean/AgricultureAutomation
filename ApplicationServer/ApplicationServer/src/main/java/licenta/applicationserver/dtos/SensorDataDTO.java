package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDataDTO {
    //describes the object used by requests using the sensor data

    private Double temperature;
    private Double humidity;
    private Double luminosity;
    private Double pressure;
    private Double soilMoisture;

    public SensorDataDTO(Double temperature, Double humidity, Double luminosity, Double pressure, Double soilMoisture){
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminosity = luminosity;
        this.pressure = pressure;
        this.soilMoisture = soilMoisture;
    }

    public SensorDataDTO(){}
}
