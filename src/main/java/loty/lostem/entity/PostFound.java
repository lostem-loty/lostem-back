package loty.lostem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostStateDTO;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class PostFound {
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
    @Size(max = 50)
    private String storage;

    @Column
    private String traderTag;

    @Column
    private LocalDateTime time;



    public static PostFound createPost(PostFoundDTO postFoundDTO, User user) {
        return PostFound.builder()
                .user(user)
                .title(postFoundDTO.getTitle())
                .images(postFoundDTO.getImages())
                .date(postFoundDTO.getDate())
                .area(postFoundDTO.getArea())
                .place(postFoundDTO.getPlace())
                .item(postFoundDTO.getItem())
                .contents(postFoundDTO.getContents())
                .state("찾는중")
                .time(LocalDateTime.now())
                .category(postFoundDTO.getCategory())
                .storage(postFoundDTO.getStorage())
                .traderTag("0")
                .build();
    }

    public void updatePostFields(PostFoundDTO postFoundDTO) {
        this.title = postFoundDTO.getTitle();
        this.category = postFoundDTO.getCategory();
        this.date = postFoundDTO.getDate();
        this.area = postFoundDTO.getArea();
        this.place = postFoundDTO.getPlace();
        this.item = postFoundDTO.getItem();
        this.contents = postFoundDTO.getContents();
        this.state = postFoundDTO.getState();
        this.time = LocalDateTime.now();
        this.storage = postFoundDTO.getStorage();
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

    public void deletePost(PostFound postFound) {
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
        this.storage = " ";
    }
}
