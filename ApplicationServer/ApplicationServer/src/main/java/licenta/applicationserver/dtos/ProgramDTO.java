package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramDTO {
    private Integer programId;
    private Integer programTypeId;
    private String programName;
    private Integer status;
    private Double temperature;
    private Double humidity;
    private Double luminosity;
}
