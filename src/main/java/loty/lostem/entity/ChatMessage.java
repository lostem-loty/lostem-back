package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.ChatMessageDTO;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "roomId")
    private ChatRoom roomId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User sender; //receiver?

    @Column
    @NotNull
    @Size(max = 1000)
    private String message;

    @Column
    @NotNull
    private LocalDateTime time;



    public static ChatMessage createChatMessage(ChatMessageDTO chatMessageDTO, ChatRoom chatRoom, User sender) {
        return ChatMessage.builder()
                .roomId(chatRoom)
                .sender(sender)
                .message(chatMessageDTO.getMessage())
                .time(LocalDateTime.now())
                .build();
    }
}