package net.l3mon.LogisticsL3mon.UserAuth.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserToListDTO {
    private String username;
    private String email;
    private String phone;
    private String company_role;
}
