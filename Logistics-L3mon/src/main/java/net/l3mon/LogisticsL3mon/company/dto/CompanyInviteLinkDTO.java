package net.l3mon.LogisticsL3mon.company.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyInviteLinkDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
}
