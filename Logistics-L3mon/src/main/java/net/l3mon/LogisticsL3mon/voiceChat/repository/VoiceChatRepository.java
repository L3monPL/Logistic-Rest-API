package net.l3mon.LogisticsL3mon.voiceChat.repository;

import net.l3mon.LogisticsL3mon.voiceChat.entity.VoiceChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoiceChatRepository extends JpaRepository<VoiceChat, Long> {
}
