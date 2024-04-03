package net.l3mon.LogisticsL3mon.Server.WebSocketConfig;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class ChannelInterceptorAdapter implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Logika wykonywana przed wysłaniem wiadomości
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        // Logika wykonywana po wysłaniu wiadomości
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        // Logika wykonywana po próbie wysłania wiadomości
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        // Logika wykonywana po otrzymaniu wiadomości
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        // Logika wykonywana po otrzymaniu wiadomości, przed jej przetworzeniem przez endpoint
        return message;
    }
}
