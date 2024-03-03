package net.l3mon.LogisticsL3mon.UserAuth.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.UserAuth.dto.UserRegisterDTO;
import net.l3mon.LogisticsL3mon.UserAuth.entity.AuthResponse;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Code;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithMail;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserExistingWithName;
import net.l3mon.LogisticsL3mon.UserAuth.exceptions.UserNullConpanyIdException;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.company.entity.Company;
import net.l3mon.LogisticsL3mon.company.repository.CompanyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Arrays;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CookiService cookiService;
    @Value("${jwt.exp}")
    private int exp;
    @Value("${jwt.refresh.exp}")
    private int refreshExp;


    private User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    private String generateToken(String username,int exp) {
        return jwtService.generateToken(username, exp);
    }

    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = cookiService.removeCookie(request.getCookies(),"Authorization");
        if (cookie != null){
            response.addCookie(cookie);
        }
        cookie = cookiService.removeCookie(request.getCookies(),"refresh");
        if (cookie != null){
            response.addCookie(cookie);
        }
        return  ResponseEntity.ok(new AuthResponse(Code.SUCCESS));
    }


    public void validateToken(HttpServletRequest request, HttpServletResponse response) throws ExpiredJwtException, IllegalArgumentException{
        String token = null;
        String refresh = null;
        for (Cookie value : Arrays.stream(request.getCookies()).toList()) {
            if (value.getName().equals("Authorization")) {
                token = value.getValue();
            } else if (value.getName().equals("refresh")) {
                refresh = value.getValue();
            }
        }
        try {
            jwtService.validateToken(token);
        }catch (IllegalArgumentException | ExpiredJwtException e){
            jwtService.validateToken(refresh);
            Cookie refreshCookie = cookiService.generateCookie("refresh", jwtService.refreshToken(refresh, refreshExp), refreshExp);
            Cookie cookie = cookiService.generateCookie("Authorization", jwtService.refreshToken(refresh, exp), exp);
            response.addCookie(cookie);
            response.addCookie(refreshCookie);
        }

    }


    public void register(UserRegisterDTO userRegisterDTO) throws UserExistingWithName, UserExistingWithMail, UserNullConpanyIdException {
        userRepository.findUserByLogin(userRegisterDTO.getLogin()).ifPresent(value->{
            throw new UserExistingWithName("Użytkownik o nazwie juz istnieje");
        });
        userRepository.findUserByEmail(userRegisterDTO.getEmail()).ifPresent(value->{
            throw new UserExistingWithMail("Użytkownik o mailu juz istnieje");
        });

        if (userRegisterDTO.getCompanyId() == null || userRegisterDTO.getCompanyId() < 0){
            throw new UserNullConpanyIdException("Nie przypisano użytkownika do firmy");
        }
        // Pobieranie obiektu Company na podstawie przekazanego companyId
        Company company = companyRepository.findById(Long.valueOf(userRegisterDTO.getCompanyId()))
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + userRegisterDTO.getCompanyId() + " not found"));

        User user = new User();
        user.setLogin(userRegisterDTO.getLogin());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());
        user.setCompany(company);
        user.setRole(Role.USER);

        saveUser(user);
    }

    public ResponseEntity<?> login(HttpServletResponse response, User authRequest) {
        User user = userRepository.findUserByLogin(authRequest.getUsername()).orElse(null);
        if (user != null) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            if (authenticate.isAuthenticated()) {
                Cookie refresh = cookiService.generateCookie("refresh", generateToken(authRequest.getUsername(),refreshExp), refreshExp);
                Cookie cookie = cookiService.generateCookie("Authorization", generateToken(authRequest.getUsername(),exp), exp);
                response.addCookie(cookie);
                response.addCookie(refresh);

//                Company company = companyRepository.findById(user.getCompany().getId())
//                        .orElseThrow(() -> new EntityNotFoundException("Company with this id not found"));

                return ResponseEntity.ok(
                        UserRegisterDTO
                                .builder()
                                .login(user.getUsername())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .companyId(Math.toIntExact(user.getCompany().getId()))
                                .build());
            } else {
                return ResponseEntity.ok(new AuthResponse(Code.A1));
            }
        }
        return ResponseEntity.ok(new AuthResponse(Code.A2));
    }

    public void setAsAdmin(UserRegisterDTO user) {
        userRepository.findUserByLogin(user.getLogin()).ifPresent(value->{
            value.setRole(Role.ADMIN);
            userRepository.save(value);
        });
    }


}
