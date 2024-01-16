package loty.lostem.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class PostLostDetailsDTO {
    private PostLostDTO postLostDTO;
    private PostUserDTO postUserDTO;
}
