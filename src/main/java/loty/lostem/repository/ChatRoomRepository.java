package loty.lostem.repository;

import loty.lostem.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    //List<ChatRoom> findByHostUser_UserId(Long userId);

    //ChatRoom findByPostIdAndGuestUserTag(Long postId, String guestUserTag);
    Optional<ChatRoom> findByPostTypeAndPostIdAndGuestUserTag(String postType, Long postId, String guestUserTag);
    /*@Query("SELECT cr FROM ChatRoom cr WHERE TYPE(cr) = LostChatRoom AND cr.postType = :postType AND cr.postId = :postId AND cr.guestUserTag = :guestUserTag")
    LostChatRoom findLostChatRoomByPostTypeAndPostIdAndGuestUserTag(@Param("postType") String postType, @Param("postId") Long postId, @Param("guestUserTag") String guestUserTag);

    @Query("SELECT cr FROM ChatRoom cr WHERE TYPE(cr) = FoundChatRoom AND cr.postType = :postType AND cr.postId = :postId AND cr.guestUserTag = :guestUserTag")
    FoundChatRoom findFoundChatRoomByPostTypeAndPostIdAndGuestUserTag(@Param("postType") String postType, @Param("postId") Long postId, @Param("guestUserTag") String guestUserTag);*/

    @Query("SELECT lr FROM ChatRoom lr WHERE lr.hostUserTag = :tag OR lr.guestUserTag = :tag")
    List<ChatRoom> findByHostUserTagOrGuestUserTag(@Param("tag") String tag);

    List<ChatRoom> findByPostTypeAndPostId(String postType, Long postId);
}