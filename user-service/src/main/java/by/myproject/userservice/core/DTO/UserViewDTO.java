package by.myproject.userservice.core.DTO;

import by.myproject.userservice.core.enums.UserRole;
import by.myproject.userservice.core.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@AllArgsConstructor
public class UserViewDTO {
    @Getter
    @NonNull
    private UUID uuid;
    @NonNull
    @Getter
    private String mail;
    @NonNull
    @Getter
    private String fio;
    @Getter
    @NonNull
    private UserRole role;
    @Getter
    private UserStatus status;
    @Getter
    private LocalDateTime dtCreate;
    @Getter
    private LocalDateTime dtUpdate;
}
