package licenta.applicationserver.repositories;

import jakarta.transaction.Transactional;
import licenta.applicationserver.entities.ControlStatus;
import licenta.applicationserver.entities.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ControlStatusRepository extends JpaRepository<ControlStatus, Integer> {
    //repository for methods related to control status operations on the database

    @Modifying
    @Transactional
    @Query("UPDATE ControlStatus c SET " +
            "c.switchControl = :switchControl, " +
            "c.fan = :fan, " +
            "c.pump = :pump, " +
            "c.led = :led " +
            "WHERE c.environment.environmentId = :environmentId")
    Integer updateControlStatus(@Param("environmentId") Integer environmentId,
                             @Param("switchControl") Integer switchControl,
                             @Param("fan") Integer fan,
                             @Param("pump") Integer pump,
                             @Param("led") Integer led);

    @Query("SELECT c FROM ControlStatus c WHERE c.environment.environmentId = :environmentId")
    Optional<ControlStatus> findControlStatusByEnvironmentId(@Param("environmentId") Integer environmentId);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("DELETE FROM ControlStatus c WHERE c.environment.environmentId = :environmentId")
    void deleteByEnvironmentId(@Param("environmentId") Integer environmentId);
}
