package com.testing.demo.controller;

import com.testing.demo.entity.Photo;
import com.testing.demo.entity.User;
import com.testing.demo.repository.PhotoRepository;
import com.testing.demo.repository.UserRepository; // Giả sử bạn có UserRepository
import com.testing.demo.service.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // Dùng để lấy user đăng nhập
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private IStorageService storageService;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository; // Giả sử bạn có UserRepository

    /**
     * Lấy user entity từ thông tin đăng nhập
     */
    private User getCurrentUser(Authentication authentication) {
        // Lấy email (được dùng như username) từ thông tin đăng nhập
        String email = authentication.getName();

        // SỬA Ở ĐÂY: Dùng findByEmail thay vì findByUsername
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /**
     * API POST /api/photos/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file,
                                         Authentication authentication) {
        try {
            // 1. Lấy user hiện tại
            User currentUser = getCurrentUser(authentication);

            // 2. Lưu file vật lý
            String filename = storageService.store(file);

            // 3. Tạo URL để truy cập file
            String photoUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(filename)
                    .toUriString();

            // 4. Lưu thông tin ảnh vào CSDL
            Photo photo = new Photo(); // Sử dụng constructor rỗng (@NoArgsConstructor)
            photo.setUser(currentUser);
            photo.setUrl(photoUrl);

            // (Nâng cao) Tính toán 'order' nếu cần
            int currentPhotoCount = currentUser.getPhotos() != null ? currentUser.getPhotos().size() : 0;
            photo.setOrder(currentPhotoCount + 1);

            Photo savedPhoto = photoRepository.save(photo);

            // 5. Trả về thông tin ảnh đã lưu (ví dụ: URL)
            // Bạn nên tạo một DTO để trả về thay vì entity
            return ResponseEntity.created(URI.create(photoUrl)).body(savedPhoto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload photo: " + e.getMessage());
        }
    }

    /**
     * API DELETE /api/photos/{photoId}
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable String photoId,
                                         Authentication authentication) {
        try {
            User currentUser = getCurrentUser(authentication);

            // 1. Tìm ảnh trong CSDL
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new RuntimeException("Photo not found"));

            // 2. Kiểm tra bảo mật: Chỉ chủ sở hữu mới được xóa
            if (!photo.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(403).body("You are not authorized to delete this photo");
            }

            // 3. Lấy tên file từ URL (cần xử lý cẩn thận)
            String url = photo.getUrl();
            String filename = url.substring(url.lastIndexOf("/") + 1);

            // 4. Xóa file vật lý
            boolean deleted = storageService.delete(filename);
            if (!deleted) {
                // Log lỗi nhưng vẫn tiếp tục để xóa khỏi CSDL
                System.err.println("Failed to delete physical file: " + filename);
            }

            // 5. Xóa record ảnh khỏi CSDL
            photoRepository.delete(photo);

            return ResponseEntity.ok().body("Photo deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete photo: " + e.getMessage());
        }
    }

    // API PUT /api/photos/reorder (Nâng cao) có thể được triển khai sau.
    // Nó sẽ nhận một danh sách ID ảnh theo thứ tự mới và cập nhật lại trường 'order'.
}