package net.l3mon.LogisticsL3mon.UserAuth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class UserRegisterDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Length(min = 5,max = 50, message = "Login powinien mieć od 5 do 50 znaków")
    private String login;
    private String email;
    @Length(min = 5,max = 75, message = "Hasło powinno skałdać się od 5 do 75 znaków")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private Integer companyId;
    private Role role;
}
