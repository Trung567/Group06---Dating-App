package com.testing.demo.service;

import com.testing.demo.entity.User;
import com.testing.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor // Sửa đổi constructor để tiêm thêm JwtService
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // <-- THÊM DÒNG NÀY

    // (Hàm registerUser của bạn vẫn ở đây...)
    public User registerUser(String email, String password, String firstName, String lastName, String phoneNumber, Date dob, String gender) {
        // ... (code cũ của bạn)
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email " + email + " đã được sử dụng!");
        }
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setDob(dob);
        newUser.setGender(gender);
        return userRepository.save(newUser);
    }

    // ===== BẮT ĐẦU LOGIC MỚI =====

    /**
     * Logic nghiệp vụ cho việc đăng nhập.
     * Trả về User nếu thành công, ném lỗi nếu thất bại.
     */
    public User loginUser(String email, String password) {

        // 1. Tìm user bằng email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        // 2. So sánh mật khẩu (quan trọng: dùng passwordEncoder.matches())
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Email hoặc mậtKẩu không đúng");
        }

        // 3. Nếu mọi thứ đều đúng, trả về user
        return user;
    }
}