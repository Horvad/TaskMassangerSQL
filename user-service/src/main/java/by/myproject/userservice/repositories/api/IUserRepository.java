package by.myproject.userservice.repositories.api;

import by.myproject.userservice.core.DTO.ContentViewDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserLoginDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.core.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public interface IUserRepository {
    void save(UUID uuid, UserCreateDTO userCreateDTO);
    ContentViewDTO getPage(int page, int size);
    UserViewDTO get(UUID uuid);
    void update(UUID uuid, LocalDateTime dtUpdate, UserCreateDTO userUpdate);
    LocalDateTime getDtUpdate(UUID uuid);
    String getPassword(String mail);
    boolean exist(String mail);
    boolean exist(UUID uuid);
    void updateStatus(String mail, UserStatus status,LocalDateTime dtUPdate);
    UserViewDTO get(String mail);
}
