package by.myproject.userservice.core.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class PersonalCreateDTO {
    @Setter
    @Getter
    @NonNull
    private String mail;
    @Setter
    @Getter
    @NonNull
    private String fio;
    @Setter
    @Getter
    @NonNull
    private String password;
}
