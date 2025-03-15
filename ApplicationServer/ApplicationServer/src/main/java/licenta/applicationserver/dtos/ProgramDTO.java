package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramDTO {
    private Integer environmentId;
    private Integer programTypeId;
    private String programName;
    private Integer status;
}
