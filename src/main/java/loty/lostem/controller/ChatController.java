package loty.lostem.controller;

import lombok.RequiredArgsConstructor;
import loty.lostem.dto.*;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final TokenProvider tokenProvider;

    // 채팅방
    @PostMapping("/room/create")
    public ResponseEntity<ChatRoomDTO> createRoom(@RequestBody ChatMessageDTO messageDTO, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatRoomDTO dto = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                dto = chatService.createRoom(messageDTO, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 목록
    @GetMapping("/room/read/user")
    public ResponseEntity<List<ChatRoomListDTO>> readUserRoom(@RequestHeader("Authorization") String authorization) {
        Long userId;
        List<ChatRoomListDTO> chatRoomListDTO= null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                chatRoomListDTO = chatService.getAllRooms(userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.ok(chatRoomListDTO);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/read/{roomId}")
    public ResponseEntity<ChatRoomSelectedDTO> selectRoom(@PathVariable Long roomId, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatRoomSelectedDTO chatRoomDTO = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                chatRoomDTO = chatService.selectRoom(roomId, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (chatRoomDTO != null) {
            return ResponseEntity.ok(chatRoomDTO);
        } else  {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/list")
    public ResponseEntity<List<ChatRoomListDTO>> getRoomListByPost(@RequestHeader("Authorization") String authorization, @RequestParam String postType, @RequestParam Long postId) {
        Long userId;
        List<ChatRoomListDTO> chatRoomListDTO= null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                chatRoomListDTO = chatService.getRoomListByPost(postType, postId, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (chatRoomListDTO != null) {
            return ResponseEntity.ok(chatRoomListDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/post")
    public ResponseEntity<Long> getRoomIdByPost(@RequestHeader("Authorization") String authorization, @RequestParam String postType, @RequestParam Long postId) {
        Long userId;
        Long roomId = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);
                roomId = chatService.getRoomIdByPost(postType, postId, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (roomId == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(roomId);
        }
    }



    @PostMapping("/message/create")
    public ResponseEntity<ChatMessageInfoDTO> createMessage(@RequestBody ChatMessageDTO messageDTO, @RequestHeader("Authorization") String authorization) {
        Long userId;
        ChatMessageInfoDTO dto = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userId = tokenProvider.getUserId(token);

                dto = chatService.createMessage(messageDTO, userId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 채팅방 채팅 내역
    @GetMapping("/get/room/{roomId}")
    public ResponseEntity<List<ChatMessageInfoDTO>> getMessages(@RequestHeader("Authorization") String authorization, @PathVariable Long roomId) {
        String userTag;
        List<ChatMessageInfoDTO> messages = new ArrayList<>();

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userTag = tokenProvider.getUserTag(token);

                messages = chatService.getAllMessages(userTag, roomId);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (messages != null) {
            return ResponseEntity.ok(messages);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/room/leave")
    public ResponseEntity<String> leaveRoom(@RequestHeader("Authorization") String authorization, @RequestBody ChatRoomIdDTO roomIdDTO) {
        String userTag;
        String check = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String token = authorization.substring(7);
                userTag = tokenProvider.getUserTag(token);
                check = chatService.leaveRoom(roomIdDTO.getRoomId(), userTag);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().body("나가기 완료");
    }
}
