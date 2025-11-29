package com.testing.demo.dto;

import com.testing.demo.entity.Photo;
import com.testing.demo.entity.Profile;
import com.testing.demo.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.ArrayList; // Import
import java.util.stream.Collectors;

@Getter
@Setter
public class ProfileResponseDto {

    private String userId;
    private String firstName;
    private String lastName;
    private int age; // <-- Chúng ta sẽ tính tuổi (thuộc tính suy diễn)
    private String bio;
    private String jobTitle;
    private List<String> photoUrls; // Danh sách các URL ảnh

    /**
     * Hàm chuyển đổi (Converter)
     * Biến 1 User (và các quan hệ) thành 1 Profile an toàn để hiển thị
     */
    public static ProfileResponseDto fromEntity(User user) {
        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        // --- Logic tính tuổi (thuộc tính suy diễn) ---
        if (user.getDob() != null) {
            LocalDate dob = new java.sql.Date(user.getDob().getTime()).toLocalDate();
            dto.setAge(Period.between(dob, LocalDate.now()).getYears());
        }

        // --- Lấy thông tin từ Profile (nếu có) ---
        Profile profile = user.getProfile();
        if (profile != null) {
            dto.setBio(profile.getBio());
            dto.setJobTitle(profile.getJobTitle());
            // (Bạn có thể thêm các trường khác như school, company...)
        }

        // --- Lấy URL ảnh (nếu có) ---
        List<Photo> photos = user.getPhotos();
        if (photos != null && !photos.isEmpty()) {
            dto.setPhotoUrls(
                    photos.stream()
                            .map(Photo::getUrl) // Lấy URL từ mỗi đối tượng Photo
                            .collect(Collectors.toList())
            );
        } else {
            dto.setPhotoUrls(new ArrayList<>()); // Trả về danh sách rỗng
        }

        return dto;
    }
}