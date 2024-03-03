package net.l3mon.LogisticsL3mon.company.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CompanyDTO {
    private Long id;
    private String name;
    private String shortName;
}
