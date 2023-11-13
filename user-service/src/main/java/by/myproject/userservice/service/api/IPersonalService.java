package by.myproject.userservice.service.api;

import by.myproject.userservice.core.DTO.PersonalCreateDTO;
import by.myproject.userservice.core.DTO.UserLoginDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;

import java.util.UUID;

public interface IPersonalService {
    void save(PersonalCreateDTO personalCreateDTO);
    void verification(String code, String mail);
    boolean login(UserLoginDTO userLoginDTO);
    UserViewDTO view(UUID uuid);
}
