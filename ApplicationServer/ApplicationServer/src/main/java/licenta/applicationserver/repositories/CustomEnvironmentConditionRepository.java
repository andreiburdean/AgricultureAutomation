package licenta.applicationserver.repositories;


import licenta.applicationserver.entities.CustomEnvironmentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CustomEnvironmentConditionRepository extends JpaRepository<CustomEnvironmentCondition, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM CustomEnvironmentCondition c WHERE c.program.programId = :programId")
    void deleteByProgramId(@Param("programId") Integer programId);

    @Modifying
    @Transactional
    @Query("UPDATE CustomEnvironmentCondition c SET c.temperature = :temperature, " +
            "c.humidity = :humidity, c.luminosity = :luminosity " +
            "WHERE c.program.programId = :programId")
    int updateCustomEnvironmentConditionByProgramId(@Param("temperature") Double temperature,
                                                                           @Param("humidity") Double humidity,
                                                                           @Param("luminosity") Double luminosity,
                                                                           @Param("programId") Integer programId);

    @Query("SELECT c FROM CustomEnvironmentCondition c WHERE c.program.programId = :programId")
    CustomEnvironmentCondition getCustomEnvironmentConditionByProgramId(@Param("programId") Integer programId);
}
