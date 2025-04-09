package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Integer> {
    @Query("SELECT e FROM Environment e WHERE e.user.userId = :userId")
    Optional<List<Environment>> findEnvironmentsByUserId(@Param("userId") Integer userId);
}

