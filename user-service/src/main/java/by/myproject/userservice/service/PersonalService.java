package by.myproject.userservice.service;

import by.myproject.userservice.core.DTO.PersonalCreateDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserLoginDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.core.enums.UserRole;
import by.myproject.userservice.core.enums.UserStatus;
import by.myproject.userservice.repositories.api.IPersonalCodeRepository;
import by.myproject.userservice.service.api.IPersonalService;
import by.myproject.userservice.service.api.IUserService;
import by.myproject.userservice.service.convertors.ConvertorPersonalCreateDTOtoUserDTOCreate;

import java.util.UUID;

public class PersonalService implements IPersonalService {
    private final ConvertorPersonalCreateDTOtoUserDTOCreate convertorToUserCreateDTO;
    private final IUserService userService;
    private final IPersonalCodeRepository codeRepository;
    public PersonalService(
            ConvertorPersonalCreateDTOtoUserDTOCreate convertorToUserCreateDTO,
            IUserService userService,
            IPersonalCodeRepository codeRepository) {
        this.convertorToUserCreateDTO = convertorToUserCreateDTO;
        this.userService = userService;
        this.codeRepository = codeRepository;
    }

    @Override
    public void save(PersonalCreateDTO personalCreateDTO) {
        if(personalCreateDTO.getFio().isEmpty()||personalCreateDTO.getFio()==null){
            throw new IllegalArgumentException("Не введено имя");
        }
        if(personalCreateDTO.getMail().isEmpty()||personalCreateDTO.getMail()==null){
            throw new IllegalArgumentException("Не введена почта");
        }
        if(personalCreateDTO.getPassword().isEmpty()||personalCreateDTO.getPassword()==null){
            throw new IllegalArgumentException("Не введен пароль");
        }
        UserCreateDTO user = convertorToUserCreateDTO.convert(personalCreateDTO);
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.WAITING_ACTIVATION);
        userService.save(user);
        String code = generateCode();
        codeRepository.save(personalCreateDTO.getMail(),code);
    }

    @Override
    public void verification(String code, String mail) {
        if(code.isEmpty()
            ||code.isBlank()
            ||mail.isEmpty()
            ||mail.isBlank()){
            throw new IllegalArgumentException("Не введен код или почта");
        }
        String codeRep = codeRepository.getCode(mail);
        if(!codeRep.equals(code)){
            throw new IllegalArgumentException("Не верный код верификации");
        }
        UserViewDTO userViewDTO = userService.get(mail);
        userService.updateStatus(mail,UserStatus.ACTIVATED,userViewDTO.getDtUpdate());
        codeRepository.delete(mail);
    }

    @Override
    public boolean login(UserLoginDTO userLoginDTO) {
        if(userLoginDTO.getMail().isEmpty()
                ||userLoginDTO.getMail().isBlank()
                ||userLoginDTO.getPassword().isEmpty()
                ||userLoginDTO.getPassword().isBlank()){
            throw new IllegalArgumentException("Не верно введен логин или пароль");
        }
        return userService.login(userLoginDTO.getMail(), userLoginDTO.getPassword());
    }

    @Override
    public UserViewDTO view(UUID uuid) {
        UserViewDTO userViewDTO = userService.get(uuid);
        if(userViewDTO==null) throw new IllegalArgumentException("Пользователя с данным UUID не существует");
        return userViewDTO;
    }

    private String generateCode(){
        int code = (int)(Math.random()*1000000);
        return String.valueOf(code);
    };
}