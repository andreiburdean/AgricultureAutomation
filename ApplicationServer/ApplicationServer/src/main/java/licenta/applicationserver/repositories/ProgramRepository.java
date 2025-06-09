package licenta.applicationserver.repositories;


import licenta.applicationserver.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    //repository for methods related to program operations on the database

    Optional<Program> findByProgramId(Integer programId);

    @Query("SELECT p FROM Program p WHERE p.environment.environmentId = :environmentId")
    Optional<List<Program>> findProgramsByEnvironmentId(@Param("environmentId") Integer environmentId);

    @Query("SELECT p FROM Program p WHERE p.environment.environmentId = :environmentId AND p.status = 1")
    Optional<Program> findActiveProgramByEnvironmentId(@Param("environmentId") Integer environmentId);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query("DELETE FROM Program p WHERE p.environment.environmentId = :environmentId")
    void deleteByEnvironmentId(@Param("environmentId") Integer environmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Program p SET p.status = 0 WHERE p.environment.environmentId = :environmentId")
    Integer stopPrograms(@Param("environmentId") Integer environmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Program p SET p.status = 0 WHERE p.programId = :programId")
    Integer stopProgram(@Param("programId") Integer programId);

    @Modifying
    @Transactional
    @Query("UPDATE Program p SET p.status = 1 WHERE p.programId = :programId")
    Integer startProgram(@Param("programId") Integer programId);
}
