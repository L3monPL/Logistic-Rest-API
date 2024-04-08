package net.l3mon.LogisticsL3mon.UserAuth.dto;

import lombok.*;
import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String login;
    private String email;
    private String phone;
    private Role role;
    private boolean isLock;
    private boolean isEnabled;
}
