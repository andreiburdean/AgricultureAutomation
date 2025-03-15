package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "environments", uniqueConstraints = @UniqueConstraint(columnNames = "raspberry_id"))
public class Environment {

    //Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "environment_id")
    private Integer environmentId;

    @Column(name = "raspberry_id", nullable = false)
    private Integer raspberryId;

    // Foreign Key
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "raspberry_ip")
    private String raspberryIP;

    @Column(name = "access_code")
    private String accessCode;
}
