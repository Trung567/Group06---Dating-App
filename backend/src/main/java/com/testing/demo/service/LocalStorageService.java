package com.testing.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils; // Cần thêm dependency: commons-io

@Service
public class LocalStorageService implements IStorageService {

    private final Path rootLocation = Paths.get("uploads"); // Thư mục gốc để lưu ảnh

    @Override
    @PostConstruct // Chạy phương thức này sau khi service được tạo
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            System.out.println("Created upload directory: " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        // 1. Kiểm tra file có phải là ảnh không
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("File is not an image. Please upload an image file.");
        }

        // 2. Tạo tên file duy nhất để tránh trùng lặp
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "." + fileExtension;

        try (InputStream inputStream = file.getInputStream()) {
            // 3. Tạo đường dẫn đến file
            Path destinationFile = this.rootLocation.resolve(uniqueFilename)
                    .normalize().toAbsolutePath();

            // 4. Copy file vào thư mục đích
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // 5. Trả về tên file duy nhất
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("Failed to delete file: " + filename + " " + e.getMessage());
            return false;
        }
    }
}