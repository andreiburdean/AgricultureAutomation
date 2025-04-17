package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionsDTO {

    public ConditionsDTO(Double temperature, Double humidity, Double luminosity){
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminosity = luminosity;
    }

    private Double temperature;
    private Double humidity;
    private Double luminosity;
}
