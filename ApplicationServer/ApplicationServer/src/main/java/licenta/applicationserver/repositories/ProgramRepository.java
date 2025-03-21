package licenta.applicationserver.repositories;


import licenta.applicationserver.entities.Environment;
import licenta.applicationserver.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    Optional<Program> findByProgramId(Integer programId);

    @Query("SELECT p FROM Program p WHERE p.environment.environmentId = :environmentId")
    Optional<List<Program>> findProgramsByEnvironmentId(@Param("environmentId") Integer environmentId);
}
