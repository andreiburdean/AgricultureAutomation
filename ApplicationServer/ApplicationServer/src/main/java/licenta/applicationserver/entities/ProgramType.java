package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "program_types")
public class ProgramType {
    //describes the entity that classifies a program based on a certain type

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_type_id")
    private Integer programTypeId;

    @Column(name = "program_type")
    private String programType;
}
