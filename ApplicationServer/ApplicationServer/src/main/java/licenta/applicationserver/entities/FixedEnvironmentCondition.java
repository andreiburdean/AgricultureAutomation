package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fixed_environment_conditions")
public class FixedEnvironmentCondition {
    //describes the fixed/builtin parameters that a program can use

    //Primary Key
    @Id
    private Integer programTypeId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programType;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "luminosity")
    private Double luminosity;
}
