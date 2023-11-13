package by.myproject.userservice.inPoint;

import by.myproject.userservice.core.DTO.PersonalCreateDTO;
import by.myproject.userservice.core.DTO.UserLoginDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.service.api.IPersonalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/users")
public class PersonalController {
    private final IPersonalService personalService;

    public PersonalController(IPersonalService personalService) {
        this.personalService = personalService;
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registration(@RequestBody PersonalCreateDTO personalCreateDTO){
        personalService.save(personalCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(path = "/verification", method = RequestMethod.GET)
    public ResponseEntity<?> verification(
            @RequestParam(name = "mail") String mail,
            @RequestParam(name = "code") String code){
        personalService.verification(code,mail);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST, consumes = "apllication/json", produces = "application/json")
    public ResponseEntity<?> login(
            @RequestBody UserLoginDTO login){
        personalService.login(login);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(path = "/me", method = RequestMethod.GET)
    public ResponseEntity<UserViewDTO> getMe(
            @RequestParam(name = "uuid") UUID uuid){
        return ResponseEntity.status(HttpStatus.OK).body(personalService.view(uuid));
    }

}
