package by.myproject.userservice.service.api;

import by.myproject.userservice.core.DTO.ContentViewDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.core.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IUserService {
    void save(UserCreateDTO userCreate);
    ContentViewDTO getPage(int page, int size);
    UserViewDTO get(UUID uuid);
    void update(UUID uuid, LocalDateTime dtUpdate, UserCreateDTO userUpdate);
    boolean login(String mail, String password);
    void updateStatus(String mail, UserStatus status, LocalDateTime dtUpdate);
    UserViewDTO get(String mail);
}
