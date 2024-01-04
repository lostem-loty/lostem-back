package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.UserDTO;
import loty.lostem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestPart("data") UserDTO userDTO, @RequestPart(value = "image", required = false)MultipartFile image) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        String checkUsername = userService.checkUsername(username);
        if (checkUsername != null) {
            return ResponseEntity.ok(checkUsername);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> selectUser(@PathVariable Long id) {
        UserDTO dto = userService.readUser(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody UserDTO userDTO) {
        UserDTO dto = userService.updateUser(userDTO);
        if (dto != null) {
            return ResponseEntity.ok("정보 수정 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        UserDTO dto = userService.deleteUser(id);
        if (dto != null) {
            return ResponseEntity.ok("유저 삭제 완료");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
