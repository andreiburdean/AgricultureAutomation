package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.ProgramType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramTypeRepository extends JpaRepository<ProgramType, Integer> {
    //repository for methods related to program type operations on the database
}
