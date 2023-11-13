package by.myproject.userservice.core.DTO;

import by.myproject.userservice.core.enums.UserRole;
import by.myproject.userservice.core.enums.UserStatus;
import lombok.*;

@AllArgsConstructor
public class UserUpdateDTO {
    @Getter
    @Setter
    @NonNull
    private String mail;
    @Getter
    @Setter
    @NonNull
    private String fio;
    @Getter
    @Setter
    @NonNull
    private UserRole role;
    @Getter
    @Setter
    @NonNull
    private UserStatus status;
    @Getter
    @Setter
    @NonNull
    private String password;
}
