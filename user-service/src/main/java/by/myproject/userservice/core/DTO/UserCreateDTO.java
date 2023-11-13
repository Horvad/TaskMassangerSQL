package by.myproject.userservice.core.DTO;

import by.myproject.userservice.core.enums.UserRole;
import by.myproject.userservice.core.enums.UserStatus;
import lombok.*;

import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class UserCreateDTO {
    @NonNull
    @Getter
    @Setter
    private String mail;
    @NonNull
    @Getter
    @Setter
    private String fio;
    @Getter
    @Setter
    private UserRole role = UserRole.USER;
    @Setter
    @Getter
    private UserStatus status = UserStatus.WAITING_ACTIVATION;
    @NonNull
    @Setter
    @Getter
    private String password;
}