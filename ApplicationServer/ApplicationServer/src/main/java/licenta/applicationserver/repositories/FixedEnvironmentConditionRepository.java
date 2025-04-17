package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.FixedEnvironmentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedEnvironmentConditionRepository extends JpaRepository<FixedEnvironmentCondition, Integer>  {
    @Query("SELECT fe FROM FixedEnvironmentCondition fe WHERE fe.programType.programTypeId = :programTypeId")
    FixedEnvironmentCondition getFixedEnvironmentConditionByProgramTypeId(@Param("programTypeId") Integer programTypeId);
}
