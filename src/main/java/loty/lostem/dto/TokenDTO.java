package loty.lostem.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class TokenDTO {

    private String accessToken;
    private String refreshToken;
}
