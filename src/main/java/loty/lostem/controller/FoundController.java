package loty.lostem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.PostFoundDTO;
import loty.lostem.dto.PostFoundListDTO;
import loty.lostem.jwt.TokenProvider;
import loty.lostem.service.FoundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/found")
public class FoundController {
    private final FoundService foundService;
    private final TokenProvider tokenProvider;

    @PostMapping("/create")
    public ResponseEntity<PostFoundDTO> createPost(HttpServletRequest request, @Valid @RequestPart("data") PostFoundDTO postFoundDTO, @RequestPart(value = "image", required = false) MultipartFile[] images) {
        String authorization = request.getHeader("Authorization");
        Long userId = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                userId = tokenProvider.getUserId(token);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }
        PostFoundDTO dto = foundService.createPost(postFoundDTO, userId);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/read/{id}") // 해당 글에 대한 정보
    public ResponseEntity<PostFoundDTO> selectPost(@PathVariable Long id) {
        PostFoundDTO dto = foundService.readPost(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<Page<PostFoundListDTO>> allLists(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostFoundListDTO> listDTOS = foundService.allLists(pageable);
        return ResponseEntity.ok(listDTOS);
    }

    @GetMapping("/read/user/{id}") // 사용자 관련 글 목록
    public ResponseEntity<List<PostFoundDTO>> userPost(@PathVariable Long id) {
        List<PostFoundDTO> dtoList = foundService.userPost(id);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /*@GetMapping("/read/search") // 검색 필터 적용 후 글 목록
    public ResponseEntity<List<PostLostDTO>> searchPost(@PathVariable PostLostDTO postDTO) {
        List<PostLostDTO> dtoList = lostService.searchPost(postDTO);
        if (dtoList != null) {
            return ResponseEntity.ok(dtoList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PatchMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody PostFoundDTO postDTO) {
        PostFoundDTO dto = foundService.updatePost(postDTO);
        if (dto != null) {
            return ResponseEntity.ok("게시물 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        PostFoundDTO dto = foundService.deletePost(id);
        if (dto != null) {
            return ResponseEntity.ok("게시물 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostFoundListDTO>> searchLost(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "start", required = false) LocalDateTime date,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "item", required = false) String item,
            @RequestParam(value = "contents", required = false) String contents,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "storage", required = false) String storage,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostFoundListDTO> listDTOS = foundService.search(title, category, date, area, place, item, contents, state, storage, pageable);

        return ResponseEntity.ok(listDTOS);
    }
}