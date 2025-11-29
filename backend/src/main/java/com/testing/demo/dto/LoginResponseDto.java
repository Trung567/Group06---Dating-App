package com.testing.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // Tạo constructor (hàm khởi tạo) cho 2 trường
public class LoginResponseDto {
    private String token; // Chuỗi JWT
    private UserResponseDto user; // Thông tin user (an toàn, file này bạn đã có)
}