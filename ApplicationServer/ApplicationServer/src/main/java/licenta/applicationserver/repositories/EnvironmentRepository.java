package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Integer> {
    Optional<Environment> findByRaspberryId(Integer raspberryId);
}

