package net.l3mon.LogisticsL3mon.UserAuth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserLoginDTO {
    private String login;
    private String email;
    private String phone;
    private Role role;
    private boolean isLock;
    private boolean isEnabled;
}
