package licenta.applicationserver.repositories;

import licenta.applicationserver.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //repository for methods related to user operations on the database

    Optional<User> findByEmail(String email);
}
