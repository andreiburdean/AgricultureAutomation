package licenta.applicationserver.services;

import licenta.applicationserver.dtos.LoginRequest;
import licenta.applicationserver.dtos.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.repositories.UserRepository;
import licenta.applicationserver.security.PasswordHasher;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUserAccount(User user) {
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public User findUserById(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    public User findByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = PasswordHasher.hashPassword(loginRequest.getPassword());
            if (user.getPassword().equals(hashedPassword)) {
                return ResponseEntity.ok(new LoginResponse(user.getUserId(), null));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, "Incorrect password"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponse(null, "User not found"));
        }
    }
}