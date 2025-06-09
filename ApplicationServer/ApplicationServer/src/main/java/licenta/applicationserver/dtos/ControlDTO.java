package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControlDTO {
    //describes the object used by the control request

    private Integer switchControl;
    private Integer fan;
    private Integer pump;
    private Integer led;

    public ControlDTO(Integer switchControl, Integer fan, Integer pump, Integer led){
        this.switchControl = switchControl;
        this.fan = fan;
        this.pump = pump;
        this.led = led;
    }
}
