package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostLostDTO;
import loty.lostem.dto.PostStateDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostLost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column
    @NotNull
    @Size(max = 20)
    private String title;

    @Convert(converter = StringArrayConverter.class)
    @Column
    @Size(max = 450)
    private List<String> images;

    @Column
    @NotNull
    @Size(max = 10)
    private String category;

    @Column
    private LocalDateTime date;

    @Column
    @NotNull
    @Size(max = 20)
    private String area;

    @Column
    @Size(max = 100)
    private String place;

    @Column
    @NotNull
    @Size(max = 30)
    private String item;

    @Column
    @NotNull
    @Size(max = 500)
    private String contents;

    @Column
    @Size(max = 5)
    private String state;

    @Column
    private String traderTag;

    @Column
    private LocalDateTime time;



    public static PostLost createPost(PostLostDTO postLostDTO, User user) {
        return PostLost.builder()
                .user(user)
                .title(postLostDTO.getTitle())
                .images(postLostDTO.getImages())
                .date(postLostDTO.getDate())
                .area(postLostDTO.getArea())
                .place(postLostDTO.getPlace())
                .item(postLostDTO.getItem())
                .contents(postLostDTO.getContents())
                .state("찾는중")
                .time(LocalDateTime.now())
                .category(postLostDTO.getCategory())
                .traderTag("0")
                .build();
    }

    public void updatePostFields(PostLostDTO postLostDTO) {
        this.title = postLostDTO.getTitle();
        this.category = postLostDTO.getCategory();
        this.date = postLostDTO.getDate();
        this.area = postLostDTO.getArea();
        this.place = postLostDTO.getPlace();
        this.item = postLostDTO.getItem();
        this.contents = postLostDTO.getContents();
        this.state = postLostDTO.getState();
        this.time = LocalDateTime.now();
    }

    public void setBasicImage() {
        this.images = Collections.singletonList("https://lostem-upload.s3.amazonaws.com/itemBasic.png");
    }

    public void updateImage(List<String> images) {
        this.images = images;
    }

    public void updatePostState(PostStateDTO postStateDTO) {
        this.state = postStateDTO.getState();
    }

    public void setTraderTag(String traderTag) {
        this.traderTag = traderTag;
    }

    public void deletePost(PostLost postLost) {
        this.title = "삭제된 게시물입니다.";
        this.images = Collections.singletonList("https://lostem-upload.s3.amazonaws.com/itemBasic.png");
        this.category = " ";
        this.date = null;
        this.area = " ";
        this.place = " ";
        this.item = " ";
        this.contents = " ";
        this.state = "삭제";
        this.time = null;
    }
}
