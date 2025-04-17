package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorDataDTO {
    private Double temperature;
    private Double humidity;
    private Double luminosity;
    private Double pressure;
    private Double soilMoisture;
}
