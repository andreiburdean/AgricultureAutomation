package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "custom_environment_conditions")
public class CustomEnvironmentCondition {

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_condition_id")
    private Integer customConditionId;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programType;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;
}
