package net.l3mon.LogisticsL3mon.UserAuth.exceptions;

public class UserNullConpanyIdException extends RuntimeException{

    public UserNullConpanyIdException(String message) {
        super(message);
    }

    public UserNullConpanyIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNullConpanyIdException(Throwable cause) {
        super(cause);
    }
}
