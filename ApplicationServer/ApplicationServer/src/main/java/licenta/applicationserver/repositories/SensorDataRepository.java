package licenta.applicationserver.repositories;

import jakarta.transaction.Transactional;
import licenta.applicationserver.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE SensorData s SET " +
            "s.temperature = :temperature, " +
            "s.humidity = :humidity, " +
            "s.luminosity = :luminosity, " +
            "s.pressure = :pressure, " +
            "s.soilMoisture = :soilMoisture " +
            "WHERE s.environment.environmentId = :environmentId")
    Integer updateSensorData(@Param("environmentId") Integer environmentId,
                             @Param("temperature") Double temperature,
                             @Param("humidity") Double humidity,
                             @Param("luminosity") Double luminosity,
                             @Param("pressure") Double pressure,
                             @Param("soilMoisture") Double soilMoisture);

    @Query("SELECT s FROM SensorData s WHERE s.environment.environmentId = :environmentId")
    Optional<SensorData> findSensorDataByEnvironmentId(@Param("environmentId") Integer environmentId);
}
