package net.l3mon.LogisticsL3mon.Server;

public class GlobalExceptionMessage extends RuntimeException{
    public GlobalExceptionMessage(String message) {
        super(message);
    }

    public GlobalExceptionMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalExceptionMessage(Throwable cause) {
        super(cause);
    }
}
