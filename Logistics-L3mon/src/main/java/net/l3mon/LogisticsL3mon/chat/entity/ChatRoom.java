package net.l3mon.LogisticsL3mon.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id
    @GeneratedValue(generator = "chat_rooms_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "chat_rooms_seq", sequenceName = "chat_rooms_seq", allocationSize = 1)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "message")
    private String message;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "reply_to_id")
    private Long replyToId;

    @Column(name = "is_edited")
    private boolean isEdited;

    @Column(name = "created_at")
    private String createdAt;
}
