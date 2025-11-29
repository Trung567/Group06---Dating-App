package com.testing.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    /**
     * Khởi tạo thư mục lưu trữ.
     */
    void init();

    /**
     * Lưu file ảnh.
     * @param file File ảnh từ request
     * @return Tên file duy nhất đã được lưu
     */
    String store(MultipartFile file);

    /**
     * Xóa file.
     * @param filename Tên file cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    boolean delete(String filename);
}