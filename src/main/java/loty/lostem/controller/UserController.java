package loty.lostem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.UserDTO;
import loty.lostem.dto.UsernameCheckDTO;
import loty.lostem.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {
        userService.createUser(userDTO);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping
    public ResponseEntity<UsernameCheckDTO> checkUsername(@RequestBody UsernameCheckDTO usernameCheckDTO) {
        UsernameCheckDTO dto = userService.checkUsername(usernameCheckDTO);
        if (dto != null) {
            return ResponseEntity.ok(usernameCheckDTO);
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
