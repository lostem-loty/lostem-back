package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.ReviewDTO;
import loty.lostem.dto.ReviewReturnDTO;
import loty.lostem.entity.PostFound;
import loty.lostem.entity.PostLost;
import loty.lostem.entity.Review;
import loty.lostem.entity.User;
import loty.lostem.repository.PostFoundRepository;
import loty.lostem.repository.PostLostRepository;
import loty.lostem.repository.ReviewRepository;
import loty.lostem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostLostRepository lostRepository;
    private final PostFoundRepository foundRepository;

    @Transactional
    public String createReview(ReviewDTO reviewDTO, Long userId) {
        User self = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        if (reviewDTO.getPostType().equals("lost")) {
            PostLost post = lostRepository.findById(reviewDTO.getPostId())
                    .orElse(null);
            if (post == null || !post.getState().equals("해결완료")) {
                return "NO DATA";
            }

            if (self.getTag().equals(post.getTraderTag())) { // 거래자일 때
                Optional<Review> optionalReview =
                        reviewRepository.findByReviewedUserTagAndPostTypeAndPostId(self.getTag(), reviewDTO.getPostType(), reviewDTO.getPostId());
                if (optionalReview.isEmpty()) {
                    Review created = Review.createReview(reviewDTO, post.getUser(), self.getTag(), self.getNickname(), "거래자");
                    reviewRepository.save(created);

                    updateStar(post.getUser().getTag(), reviewDTO.getStar());
                    return "OK";
                } else return "Existing";
            } else if (self.getUserId().equals(post.getUser().getUserId())) { // 글쓴이일 때
                User trader = userRepository.findByTag(post.getTraderTag())
                        .orElseThrow(() -> new IllegalArgumentException("No user"));

                Optional<Review> optionalReview =
                        reviewRepository.findByReviewedUserTagAndPostTypeAndPostId(self.getTag(), reviewDTO.getPostType(), reviewDTO.getPostId());
                if (optionalReview.isEmpty()) {
                    Review created = Review.createReview(reviewDTO, trader, self.getTag(), self.getNickname(), "작성자");
                    reviewRepository.save(created);

                    updateStar(trader.getTag(), reviewDTO.getStar());
                    return "OK";
                } else return "Existing";
            } else return "Fail";
        } else if (reviewDTO.getPostType().equals("found")) {
            PostFound post = foundRepository.findById(reviewDTO.getPostId())
                    .orElse(null);
            if (post == null || !post.getState().equals("해결완료")) {
                return "NO DATA";
            }

            if (self.getTag().equals(post.getTraderTag())) {
                Optional<Review> optionalReview =
                        reviewRepository.findByReviewedUserTagAndPostTypeAndPostId(self.getTag(), reviewDTO.getPostType(), reviewDTO.getPostId());
                if (optionalReview.isEmpty()) {
                    Review created = Review.createReview(reviewDTO, post.getUser(), self.getTag(), self.getNickname(), "거래자");
                    reviewRepository.save(created);

                    updateStar(post.getUser().getTag(), reviewDTO.getStar());
                    return "OK";
                } else return "Existing";
            } else if (self.getUserId().equals(post.getUser().getUserId())) {
                User trader = userRepository.findByTag(post.getTraderTag())
                        .orElseThrow(() -> new IllegalArgumentException("No user"));

                Optional<Review> optionalReview =
                        reviewRepository.findByReviewedUserTagAndPostTypeAndPostId(self.getTag(), reviewDTO.getPostType(), reviewDTO.getPostId());
                if (optionalReview.isEmpty()) {
                    Review created = Review.createReview(reviewDTO, trader, self.getTag(), self.getNickname(), "작성자");
                    reviewRepository.save(created);

                    updateStar(trader.getTag(), reviewDTO.getStar());
                    return "OK";
                } else return "Existing";
            } else return "Fail";
        } else
            return "Fail";
    }

    // 상세 보기는 지원하지 않음. 전체 목록만
    public List<ReviewReturnDTO> readReview(String tag) {
        List<Review> reviews = reviewRepository.findByUser_TagOrderByTimeDesc(tag);

        List<ReviewReturnDTO> reviewReturnDTOS = new ArrayList<>();
        for (Review review : reviews) {
            User reviewedUser = userRepository.findByTag(review.getReviewedUserTag()).get();
            ReviewReturnDTO reviewReturnDTO = ReviewReturnDTO.builder()
                    .reviewedUserTag(review.getReviewedUserTag())
                    .reviewedNickname(reviewedUser.getNickname())
                    .profile(reviewedUser.getProfile())
                    .role(review.getRole())
                    .contents(review.getContents())
                    .time(review.getTime())
                    .build();
            reviewReturnDTOS.add(reviewReturnDTO);
        }

        return reviewReturnDTOS;
    }

    @Transactional
    public String deleteReview(Long reviewId, String userTag) {
        Review selectedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("No data found"));

        if (selectedReview.getReviewedUserTag().equals(userTag)) {
            reviewRepository.deleteById(reviewId);
            return "OK";
        } else {
            return "Fail";
        }
    }

    @Transactional
    public void updateStar(String userTag, float star) {
        User user = userRepository.findByTag(userTag)
                .orElseThrow(() -> new IllegalArgumentException("No user"));
        user.updateStar(star);
        userRepository.save(user);
    }
}
