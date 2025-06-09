package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "programs")
public class Program {
    //describes the entity that hold the parameters as a "program" that runs on the RPi5

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Integer programId;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "environment_id", referencedColumnName = "environment_id", nullable = false)
    private Environment environment;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "program_type_id", nullable = false)
    private ProgramType programType;

    @Column(name = "program_name")
    private String programName;
    @Column(name = "status")
    private Integer status;
}
