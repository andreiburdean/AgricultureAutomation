package licenta.applicationserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramDTO {
    //describes the object used by requests operating on programs

    private Integer programId;
    private Integer programTypeId;
    private String programName;
    private Integer status;
    private Double temperature;
    private Double humidity;
    private Double luminosity;

    public ProgramDTO(Integer programId, Integer programTypeId, String programName, Integer status, Double temperature, Double humidity, Double luminosity){
        this.programId = programId;
        this.programTypeId = programTypeId;
        this.programName = programName;
        this.status = status;
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminosity = luminosity;
    }

    public ProgramDTO(){}
}
