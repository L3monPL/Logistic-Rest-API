package net.l3mon.LogisticsL3mon.UserAuth.entity;

import lombok.Getter;

import java.sql.Timestamp;
//@Data
//@Builder
@Getter
public class AuthResponse {
    private final String timestamp;
    private final String message;
    private final Code code;

    public AuthResponse(Code code) {
        this.timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()));
        this.message = code.label;
        this.code = code;
    }

}
