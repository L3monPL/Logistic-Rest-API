package net.l3mon.LogisticsL3mon.Server.WebSocketConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.l3mon.LogisticsL3mon.Server.JwtService;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public WebSocketConfig(UserRepository userRepository, @Value("${jwt.secret}") String secret){
        this.userRepository = userRepository;
        SECRET = secret;
    }

    private final UserRepository userRepository;
    public final String SECRET;
    @Autowired
    private JwtService jwtService;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    String token = accessor.getFirstNativeHeader("Authorization");
//                    System.out.println(token);
//                    System.out.println(Objects.requireNonNull(accessor.getUser()).getName());
                    String username = Objects.requireNonNull(accessor.getUser()).getName();
                    if (username != null) {
//                        if (parseToken(token) != null) {
//                            String userLogin = parseToken(token);
//                            System.out.println(userLogin);
                            User user = userRepository.findUserByLogin(username).orElse(null);
                            assert user != null;
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
                            accessor.setUser(authentication);
//                        }
                    }
                }
                return super.preSend(message, channel);
            }
        });
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
