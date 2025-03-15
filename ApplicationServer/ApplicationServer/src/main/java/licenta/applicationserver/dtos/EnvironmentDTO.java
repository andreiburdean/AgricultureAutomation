package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvironmentDTO {
    private Integer raspberryId;
    private String raspberryIp;
    private Integer userId;
    private String accessCode;
}

