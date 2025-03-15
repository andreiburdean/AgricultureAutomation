package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fixed_environment_conditions")
public class FixedEnvironmentCondition {

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fixed_condition_id")
    private Integer fixedConditionId;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programType;
}
