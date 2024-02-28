package net.l3mon.LogisticsL3mon.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private Role role;
}
