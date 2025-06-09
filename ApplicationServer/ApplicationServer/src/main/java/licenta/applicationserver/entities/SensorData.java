package licenta.applicationserver.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sensor_data")
public class SensorData {
    //describes the entity that represents the sensor data

    //Primary Key
    @Id
    private Integer environmentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "environment_id", nullable = false)
    private Environment environment;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "luminosity")
    private Double luminosity;

    @Column(name = "pressure")
    private Double pressure;

    @Column(name = "soil_moisture")
    private Double soilMoisture;
}
