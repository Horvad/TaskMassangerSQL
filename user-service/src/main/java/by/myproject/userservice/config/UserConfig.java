package by.myproject.userservice.config;

import by.myproject.userservice.config.properties.DataSourceProperty;
import by.myproject.userservice.config.properties.JWTProperty;
import by.myproject.userservice.repositories.PersonalCodeRepository;
import by.myproject.userservice.repositories.UserRepository;
import by.myproject.userservice.repositories.api.IPersonalCodeRepository;
import by.myproject.userservice.repositories.api.IUserRepository;
import by.myproject.userservice.repositories.dataSource.DataSourceC3P0;
import by.myproject.userservice.repositories.dataSource.api.IDataSourceWrapper;
import by.myproject.userservice.service.PersonalService;
import by.myproject.userservice.service.UserService;
import by.myproject.userservice.service.api.IPersonalService;
import by.myproject.userservice.service.api.IUserService;
import by.myproject.userservice.service.convertors.ConvertorPersonalCreateDTOtoUserDTOCreate;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.beans.PropertyVetoException;

@SpringBootApplication
@EnableConfigurationProperties({DataSourceProperty.class, JWTProperty.class})
@Configuration
//@AutoConfigureBefore({JacksonAutoConfiguration.class})
public class UserConfig {
    @Bean
    public IUserService userService(IUserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public IUserRepository userRepository(IDataSourceWrapper dataSourceWrapper){
        return new UserRepository(dataSourceWrapper);
    }

    @Bean
    public IDataSourceWrapper dataSourceWrapper(DataSourceProperty dataSourceProperty) throws PropertyVetoException {
        return new DataSourceC3P0(
                dataSourceProperty.getDriver(),
                dataSourceProperty.getUrl(),
                dataSourceProperty.getUsername(),
                dataSourceProperty.getPassword());
    }

    @Bean
    public IPersonalService personalService(
            ConvertorPersonalCreateDTOtoUserDTOCreate convertorPersonalCreateDTOtoUserDTOCreate,
            IUserService userService,
            IPersonalCodeRepository personalCodeRepository){
        return new PersonalService(
                convertorPersonalCreateDTOtoUserDTOCreate,
                userService,
                personalCodeRepository
        );
    }

    @Bean
    public ConvertorPersonalCreateDTOtoUserDTOCreate convertorPersonalCreateDTOtoUserDTOCreate(){
        return new ConvertorPersonalCreateDTOtoUserDTOCreate();
    }

    @Bean IPersonalCodeRepository personalCodeRepository(IDataSourceWrapper dataSourceWrapper){
        return new PersonalCodeRepository(dataSourceWrapper);
    }

    @Bean
    public PasswordE
}
