package by.myproject.userservice.inPoint;

import by.myproject.userservice.core.DTO.ContentViewDTO;
import by.myproject.userservice.core.DTO.UserCreateDTO;
import by.myproject.userservice.core.DTO.UserViewDTO;
import by.myproject.userservice.service.api.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addUser(@RequestBody UserCreateDTO userCreate){
        userService.save(userCreate);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ContentViewDTO> getPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size){
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.getPage(page,size)
        );
    }

    @RequestMapping(path = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<UserViewDTO> getUser(@PathVariable("uuid") UUID uuid){
        return ResponseEntity.status(HttpStatus.OK).body(userService.get(uuid));
    }

    @RequestMapping(path = "/{uuid}/dt_update/dt_update", method = RequestMethod.PUT)
    public ResponseEntity<?> update(
            @PathVariable("uuid") UUID uuid,
            @PathVariable("dt_update")LocalDateTime dtUpdate,
            @RequestBody UserCreateDTO userUpdate
            ){
        userService.update(uuid,dtUpdate,userUpdate);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
