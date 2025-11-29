package com.testing.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.sql.Timestamp;

// ----- THỰC THỂ CỐT LÕI -----

// ----- THỰC THỂ TƯƠNG TÁC (QUAN HỆ ĐỆ QUY) -----

// ----- THỰC THỂ CHAT -----

// ----- THỰC THỂ MỞ RỘNG (PROMPT & INTEREST) -----

// Ghi chú: Bảng "User_Interests" được quản lý tự động bởi @ManyToMany trong User

// ----- THỰC THỂ QUẢN LÝ (MODERATION) -----

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
