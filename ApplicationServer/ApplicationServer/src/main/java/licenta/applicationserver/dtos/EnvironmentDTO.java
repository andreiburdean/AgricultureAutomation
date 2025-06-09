package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnvironmentDTO {
    //describes the object used by requests operating on environments

    private Integer environmentId;
    private Integer raspberryId;
    private String raspberryIp;
    private String environmentName;

    public EnvironmentDTO(Integer environmentId, Integer raspberryId, String raspberryIp, String environmentName) {
        this.environmentId = environmentId;
        this.raspberryId = raspberryId;
        this.raspberryIp = raspberryIp;
        this.environmentName = environmentName;
    }
}

