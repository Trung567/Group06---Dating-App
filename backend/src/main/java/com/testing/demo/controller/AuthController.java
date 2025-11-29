package com.testing.demo.controller;

// Thêm các DTO và Service mới
import com.testing.demo.dto.LoginRequestDto;
import com.testing.demo.dto.LoginResponseDto;
import com.testing.demo.dto.RegisterRequestDto;
import com.testing.demo.dto.UserResponseDto;
import com.testing.demo.entity.User;
import com.testing.demo.service.AuthService;
import com.testing.demo.service.JwtService; // <-- THÊM DÒNG NÀY
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Sửa đổi constructor để tiêm thêm JwtService
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService; // <-- THÊM DÒNG NÀY

    // (API /register của bạn vẫn ở đây...)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequest) {
        try {
            User newUser = authService.registerUser(
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName(),
                    registerRequest.getPhoneNumber(),
                    registerRequest.getDob(),
                    registerRequest.getGender()
            );
            UserResponseDto responseDto = UserResponseDto.fromEntity(newUser);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== BẮT ĐẦU API MỚI =====

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) {
        try {
            // 1. Gọi Service để xác thực
            User authenticatedUser = authService.loginUser(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            // 2. Nếu xác thực thành công, tạo Token
            String token = jwtService.generateToken(authenticatedUser);

            // 3. Chuyển User sang DTO an toàn
            UserResponseDto userDto = UserResponseDto.fromEntity(authenticatedUser);

            // 4. Trả về Token và thông tin User
            LoginResponseDto response = new LoginResponseDto(token, userDto);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Nếu Service ném lỗi (sai pass, sai email)
            return ResponseEntity.status(401).body(e.getMessage()); // 401 Unauthorized
        }
    }
}