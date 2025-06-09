package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "control_status")
public class ControlStatus {
    //describes the structure of the control request

    //Primary Key
    @Id
    private Integer environmentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "environment_id", nullable = false)
    private Environment environment;

    @Column(name = "switch_control")
    private Integer switchControl;

    @Column(name = "fan")
    private Integer fan;

    @Column(name = "pump")
    private Integer pump;

    @Column(name = "led")
    private Integer led;
}
