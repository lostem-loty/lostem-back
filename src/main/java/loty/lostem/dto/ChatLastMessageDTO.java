package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ChatLastMessageDTO {
    @NotNull
    @Size(max = 1000)
    private String message;

    private LocalDateTime time;
}
