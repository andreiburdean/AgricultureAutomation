package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramTypeRepository extends JpaRepository<ProgramType, Integer> {
}
