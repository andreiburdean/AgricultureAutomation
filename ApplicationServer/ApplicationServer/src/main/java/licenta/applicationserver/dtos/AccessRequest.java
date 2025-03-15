package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRequest {
    private Integer raspberryId;
    private String accessCode;

    public AccessRequest(Integer raspberryId, String accessCode){
        this.raspberryId = raspberryId;
        this.accessCode = accessCode;
    }
}
