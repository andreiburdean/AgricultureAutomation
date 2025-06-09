package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "custom_environment_conditions")
public class CustomEnvironmentCondition {
    //describes the custom parameters a user can input into a program

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_condition_id")
    private Integer customConditionId;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @ManyToOne
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programType;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "luminosity")
    private Double luminosity;
}
