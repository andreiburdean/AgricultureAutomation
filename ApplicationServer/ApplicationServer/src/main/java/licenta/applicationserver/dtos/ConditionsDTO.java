package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionsDTO {
    //describes the object used by the requests that send the parameters/conditions

    private Double temperature;
    private Double humidity;
    private Double luminosity;

    public ConditionsDTO(Double temperature, Double humidity, Double luminosity){
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminosity = luminosity;
    }
}
