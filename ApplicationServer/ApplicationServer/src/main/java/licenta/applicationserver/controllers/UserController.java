package licenta.applicationserver.controllers;

import licenta.applicationserver.dtos.LoginRequestDTO;
import licenta.applicationserver.dtos.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import licenta.applicationserver.entities.User;
import licenta.applicationserver.services.UserService;

@Controller
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create-user")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            userService.createUserAccount(user);
            System.out.println("New user created: " + user.getUserId() + ", " + user.getEmail() + ", " + user.getPassword());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.loginUser(loginRequestDTO);
    }
}
