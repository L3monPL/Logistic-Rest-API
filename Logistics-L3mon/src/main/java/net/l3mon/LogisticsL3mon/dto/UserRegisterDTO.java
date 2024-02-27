package net.l3mon.LogisticsL3mon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.entity.Role;
@Getter
@Setter
@Builder
public class UserRegisterDTO {
    private String login;
    private String email;
    private String password;
    private Role role;
}
