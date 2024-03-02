package net.l3mon.LogisticsL3mon.UserAuth.exceptions;

public class UserExistingWithMail extends RuntimeException{
    public UserExistingWithMail(String message) {
        super(message);
    }

    public UserExistingWithMail(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistingWithMail(Throwable cause) {
        super(cause);
    }
}

