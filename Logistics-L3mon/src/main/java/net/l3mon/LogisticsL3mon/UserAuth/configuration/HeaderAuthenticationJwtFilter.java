package net.l3mon.LogisticsL3mon.UserAuth.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeaderAuthenticationJwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    public final String SECRET = "AD01FDF92498FF62C1B9E7E9193CCCCE8EAB8E50CEE980BC7BD72FB9A49B5262";
    public static final String USER = "USER";
    public static final String USER_AUTH_EXCEPTIONS = "USER_AUTH_EXCEPTIONS";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String authHeader = null;
            Cookie[] cookies = request.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        authHeader = cookie.getValue();
                        break; // Exit loop once the Authorization cookie is found
                    }
                }
            }
//            log.info("Authorization: " + authHeader);

            if (authHeader == null || authHeader.trim().isEmpty()) {
                request.setAttribute(USER_AUTH_EXCEPTIONS, "Brak uprawnień");
                filterChain.doFilter(request, response);
                return;
            }
            log.info(authHeader);

            String userLogin = parseToken(authHeader);
            log.info("User Login: " + userLogin);

            User user = userRepository.findUserByLogin(userLogin).orElse(null);

            assert user != null;
            log.info(String.valueOf(user.getRole()));

            org.springframework.security.core.userdetails.User authUser = new org.springframework.security.core.userdetails.User(
                    user.getLogin(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()))
            );

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    authUser.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.setAttribute(USER, user);

//            authUser.getAuthorities().forEach(authority ->
//                    System.out.println("Użytkownik ma rolę: " + authority.getAuthority())
//            );

            filterChain.doFilter(request, response);

        }catch (Exception e){
            request.setAttribute(USER_AUTH_EXCEPTIONS, "Token nie jest ważny");
            filterChain.doFilter(request, response);
        }
    }

    private String parseToken(String token) {
        Claims decodeTokenBody = Jwts.parserBuilder()
                .setSigningKey(SECRET)
                .build()
                .parseClaimsJws(token.replace("Bearer ", "").trim())
                .getBody();

        return decodeTokenBody.getSubject();
    }
}
