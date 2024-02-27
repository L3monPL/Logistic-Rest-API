package net.l3mon.LogisticsL3mon.fasada;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.dto.UserRegisterDTO;
import net.l3mon.LogisticsL3mon.entity.AuthResponse;
import net.l3mon.LogisticsL3mon.entity.Code;
import net.l3mon.LogisticsL3mon.entity.User;
import net.l3mon.LogisticsL3mon.exceptions.UserExistingWithMail;
import net.l3mon.LogisticsL3mon.exceptions.UserExistingWithName;
import net.l3mon.LogisticsL3mon.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response){
        return userService.login(response,user);
    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<AuthResponse> addNewUser(@RequestBody UserRegisterDTO user){
        try{
            log.info("--START REGISTER USER");
            userService.register(user);
            log.info("--STOP REGISTER USER");
            return ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
        }catch (UserExistingWithName e){
            log.info("User don't exist in database");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A4));
        }catch (UserExistingWithMail existing){
            log.info("User don't exist in database with this mail");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(Code.A5));
        }

    }

}
