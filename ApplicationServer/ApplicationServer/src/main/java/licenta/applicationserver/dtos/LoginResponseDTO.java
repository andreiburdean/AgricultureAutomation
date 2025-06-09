package licenta.applicationserver.dtos;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    //describes the login response

    private final Integer userId;
    private final String message;

    public LoginResponseDTO(Integer userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
