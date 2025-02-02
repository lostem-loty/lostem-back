package loty.lostem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ReviewReturnDTO {
    private String reviewedUserTag;  // 평가하는 사람
    private String reviewedNickname;

    private String role; // 작성자 || 거래자
    private String profile;

    @Size(max = 100)
    private String contents;  // 내용

    private LocalDateTime time;
}
