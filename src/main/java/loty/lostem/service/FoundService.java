package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.User;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.UserRepository;
import loty.lostem.search.FoundSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoundService {
    private final PostFoundRepository postFoundRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostFoundDTO createPost(PostFoundDTO postFoundDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found for the provided id"));
        PostFound created = PostFound.createPost(postFoundDTO, user);
        postFoundRepository.save(created);
        PostFoundDTO createdDTO = postToDTO(created);
        return createdDTO;
    }

    // 하나의 게시물에 대한 정보 리턴
    public PostFoundDetailsDTO readPost(Long postId) {
        PostFound selectPost = postFoundRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        User user = userRepository.findById(selectPost.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No data for user"));
        PostUserDTO readOnePost = postUserToDTO(user);
        PostFoundDTO selectedDTO = postToDTO(selectPost);
        PostFoundDetailsDTO postFoundDetailsDTO =
                PostFoundDetailsDTO.builder()
                        .postFoundDTO(selectedDTO)
                        .postUserDTO(readOnePost)
                        .build();
        return postFoundDetailsDTO;
    }

    // 전체 목록 보기
    public Page<PostFoundListDTO> allLists(Pageable pageable) {
        return postFoundRepository.findAll(pageable)
                .map(this::listToDTO);
    }

    public List<PostFoundDTO> userPost(String tag) {
        return postFoundRepository.findByUser_Tag(tag).stream()
                .map(this::postToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostFoundDTO updatePost(PostFoundDTO postDTO) {
        PostFound selectedPost = postFoundRepository.findById(postDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedPost.updatePostFields(postDTO);
        postFoundRepository.save(selectedPost);
        PostFoundDTO changedDTO = postToDTO(selectedPost);
        return changedDTO;
    }

    @Transactional
    public PostFoundDTO updateState(Long userId, PostStateDTO stateDTO) {
        PostFound selectedPost = postFoundRepository.findById(stateDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (!userId.equals(selectedPost.getUser().getUserId())) {
            return null;
        }

        selectedPost.updatePostState(stateDTO);
        postFoundRepository.save(selectedPost);
        PostFoundDTO changedDTO = postToDTO(selectedPost);
        return changedDTO;
    }

    @Transactional
    public PostFoundDTO deletePost(Long postId, Long userId) {
        PostFound selectedPost = postFoundRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));

        if (selectedPost.getUser().getUserId().equals(userId)) {
            selectedPost.deletePost(selectedPost);
            postFoundRepository.save(selectedPost);
            PostFoundDTO selectedDTO = postToDTO(selectedPost);
            return selectedDTO;
        } else {
            return null;
        }
    }

    public Page<PostFoundListDTO> search(String title, String category, LocalDateTime date,
                                         String area, String place, String item, String contents, String state, String storage, Pageable pageable) {
        Specification<PostFound> spec = (root, query, criteriaBuilder) -> null;

        if (title != null)
            spec = spec.and(FoundSpecification.likeTitle(title));
        if (category != null)
            spec = spec.and(FoundSpecification.equalCategory(category));
        if (date != null)
            spec = spec.and(FoundSpecification.equalDate(date));
        if (area != null)
            spec = spec.and(FoundSpecification.equalArea(area));
        if (place != null)
            spec = spec.and(FoundSpecification.likePlace(place));
        if (item != null)
            spec = spec.and(FoundSpecification.equalItem(item));
        if (contents != null)
            spec = spec.and(FoundSpecification.likeContents(contents));
        if (state != null)
            spec = spec.and(FoundSpecification.equalState(state));
        if (storage != null)
            spec = spec.and(FoundSpecification.likeStorage(storage));

        return postFoundRepository.findAll(spec, pageable)
                .map(this::listToDTO);
    }


    public PostFoundDTO postToDTO(PostFound post) {
        return PostFoundDTO.builder()
                .postId(post.getPostId())
                .userId(post.getUser().getUserId())
                .title(post.getTitle())
                .images(post.getImages())
                .date(post.getDate())
                .area(post.getArea())
                .place(post.getPlace())
                .item(post.getItem())
                .contents(post.getContents())
                .state(post.getState())
                .report(post.getReport())
                .time(post.getTime())
                .category(post.getCategory())
                .build();
    }

    public PostFoundListDTO listToDTO(PostFound post) {
        return PostFoundListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .image(post.getImages())
                .area(post.getArea())
                .time(post.getTime())
                .build();
    }

    public PostUserDTO postUserToDTO(User user) {
        return PostUserDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .tag(user.getTag())
                .build();
    }
}
