package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class UserPreviewDTO {
    private String nickname;

    private String profile;

    private double star;

    private String tag;
}
