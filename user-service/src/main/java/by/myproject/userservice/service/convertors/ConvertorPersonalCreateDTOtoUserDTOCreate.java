package by.myproject.userservice.service.convertors;

import by.myproject.userservice.core.DTO.PersonalCreateDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import org.springframework.core.convert.converter.Converter;

public class ConvertorPersonalCreateDTOtoUserDTOCreate implements Converter<PersonalCreateDTO, UserCreateDTO> {
    @Override
    public UserCreateDTO convert(PersonalCreateDTO source) {
        UserCreateDTO user = new UserCreateDTO();
        user.setFio(source.getFio());
        user.setMail(source.getPassword());
        user.setPassword(source.getPassword());
        return user;
    }
}
