package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fixed_environment_conditions")
public class FixedEnvironmentCondition {

    //Primary Key/ Foreign Key
    @Id
    @OneToOne(optional = false)
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programTypeId;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "luminosity")
    private Double luminosity;
}
