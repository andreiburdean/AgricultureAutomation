package licenta.applicationserver.services;

import licenta.applicationserver.dtos.LoginRequestDTO;
import licenta.applicationserver.dtos.LoginResponseDTO;
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

    public void createUserAccount(User user) {
        String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);
    }

    public User findUserById(Integer userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElse(null);
    }

    public User findByEmail(String email){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.orElse(null);
    }

    public ResponseEntity<LoginResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDTO.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String hashedPassword = PasswordHasher.hashPassword(loginRequestDTO.getPassword());
            if (user.getPassword().equals(hashedPassword)) {
                System.out.println("User logged in: " + loginRequestDTO.getEmail() + " " + loginRequestDTO.getPassword());
                return ResponseEntity.ok(new LoginResponseDTO(user.getUserId(), null));
            } else {
                System.out.println("Failed login. Reason: wrong password for user: " + user.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDTO(null, "Incorrect password"));
            }
        } else {
            System.out.println("Failed login. Reason: user " + loginRequestDTO.getEmail() + " doesn't exist.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new LoginResponseDTO(null, "User not found"));
        }
    }
}