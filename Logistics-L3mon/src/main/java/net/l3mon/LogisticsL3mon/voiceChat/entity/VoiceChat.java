package net.l3mon.LogisticsL3mon.voiceChat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voice_chat")
public class VoiceChat {
    @Id
    @GeneratedValue(generator = "voice_chats_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "voice_chats_seq", sequenceName = "voice_chats_seq", allocationSize = 1)
    private Long id;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "company_id")
    private Long companyId;

//    @ElementCollection
//    private List<Long> participantIds = new ArrayList<>();
}
