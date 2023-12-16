package loty.lostem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import loty.lostem.dto.LoginDTO;
import loty.lostem.dto.UserDTO;
import loty.lostem.dto.UsernameCheckDTO;
import loty.lostem.entity.RefreshToken;
import loty.lostem.entity.User;
import loty.lostem.repository.RefreshTokenRepository;
import loty.lostem.repository.UserRepository;
import loty.lostem.security.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        String encoded = bCryptPasswordEncoder.encode(userDTO.getPassword());
        UserDTO.setPasswordEncode(userDTO, encoded);
        User created = User.createUser(userDTO);
        userRepository.save(created);
        return userDTO;
    }

    public UsernameCheckDTO checkUsername(UsernameCheckDTO usernameCheckDTO) {
        if (userRepository.findByUsername(usernameCheckDTO.getUsername()).isEmpty()) {
            return usernameCheckDTO;
        } else {
            return null;
        }
    }

    public UserDTO readUser(Long userId) { // 프로필 정보 확인 창
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        UserDTO selectedDTO = userToDTO(selectedUser);
        return selectedDTO;
    }

    public UserDTO loginUser(LoginDTO loginDTO) {
        User loginUser = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided username"));

        if (bCryptPasswordEncoder.matches(loginDTO.getPassword(), loginUser.getPassword())) {
            UserDTO userDTO = userToDTO(loginUser);
            UserDTO.setPasswordNull(userDTO);
            return userDTO;
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
    }

    public Long loginData(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided token"));
        User loginUser = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user data found for the provided token"));
        UserDTO userDTO = userToDTO(loginUser);
        return refreshToken.getUserId();
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User selectedUser = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        selectedUser.updateUserFields(selectedUser, userDTO);
        userRepository.save(selectedUser);
        UserDTO changedDTO = userToDTO(selectedUser);
        return changedDTO;
    }

    @Transactional
    public UserDTO deleteUser(Long userId) {
        User selectedUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No data found for the provided id"));
        UserDTO selectedDTO = userToDTO(selectedUser);
        userRepository.deleteById(userId);
        return selectedDTO;
    }



    public UserDTO userToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .password(user.getPassword())
                .phone(user.getPhone())
                .email(user.getEmail())
                .profile(user.getProfile())
                .star(user.getStar())
                .starCount(user.getStarCount())
                .tag(user.getTag())
                .role(UserRole.valueOf(user.getRole()))
                .build();
    }
}
