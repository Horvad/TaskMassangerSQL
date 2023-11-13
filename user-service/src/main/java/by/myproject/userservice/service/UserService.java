package by.myproject.userservice.service;

import by.myproject.userservice.core.DTO.ContentViewDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserLoginDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.core.enums.UserStatus;
import by.myproject.userservice.repositories.api.IUserRepository;
import by.myproject.userservice.service.api.IUserService;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserService implements IUserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserCreateDTO userCreate) {
        if(userRepository.exist(userCreate.getMail()))
            throw new IllegalArgumentException("Данная почта существует");
        UUID uuid = UUID.randomUUID();
        while (userRepository.exist(uuid)){
            uuid = UUID.randomUUID();
        }
        userRepository.save(uuid,userCreate);
    }

    @Override
    public ContentViewDTO getPage(int page, int size) {
        if(page<0||size<1){
            throw new IllegalArgumentException("Не верно введены page or size");
        }
        return userRepository.getPage(page,size);
    }

    @Override
    public UserViewDTO get(UUID uuid) {
        UserViewDTO userViewDTO = userRepository.get(uuid);
        if(userViewDTO==null){
            throw new NullPointerException("Данного пользователя не существует");
        }
        return userViewDTO;
    }

    @Override
    public void update(UUID uuid, LocalDateTime dtUpdate, UserCreateDTO userUpdate) {
        LocalDateTime dtUpdateRep = userRepository.getDtUpdate(uuid);
        if(!dtUpdateRep.isEqual(dtUpdate))
            throw new IllegalArgumentException("Не совпадают даты обновления");
        if(!userRepository.exist(uuid))
            throw new IllegalArgumentException("Данного пользователя не существует");
        userRepository.update(uuid,dtUpdate,userUpdate);
    }

    @Override
    public boolean login(String mail, String password) {
        if(!userRepository.exist(mail)){
            throw new IllegalArgumentException("Не верно введен пользователь или пароль");
        }
        String passwordRep = userRepository.getPassword(mail);
        if(!passwordRep.equals(password))
            throw new IllegalArgumentException("Не верно введен пользователь или пароль");
        return true;
    }

    @Override
    public void updateStatus(String mail, UserStatus status, LocalDateTime dtUpdate) {
        if(mail==null
            ||mail.isEmpty()
            ||mail.isBlank()){
            throw new IllegalArgumentException("Не верная почта");
        }
        UserViewDTO userViewDTO = userRepository.get(mail);
        if(userViewDTO.getDtUpdate().isEqual(dtUpdate)) {
            userRepository.updateStatus(mail, status, dtUpdate);
        } else {
            throw new IllegalArgumentException("Не верная версия");
        }
    }

    @Override
    public UserViewDTO get(String mail) {
        if(mail==null||mail.isEmpty()||mail.isEmpty()) {
            throw new IllegalArgumentException("Не введена почта");
        }
        return userRepository.get(mail);
    }
}
