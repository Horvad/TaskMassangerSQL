package by.myproject.userservice.core.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
public class UserLoginDTO {
    @NonNull
    @Getter
    private String mail;
    @NonNull
    @Getter
    private String password;
}
