package licenta.applicationserver.dtos;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final Integer userId;
    private final String message;

    public LoginResponse(Integer userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
