package com.testing.demo.service;

import com.testing.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Dịch vụ này CHỈ dành cho Spring Security.
 * Nó triển khai (implements) UserDetailsService để "dạy" Spring cách
 * tải User từ CSDL bằng cách sử dụng UserRepository.
 */
@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // "username" trong Spring Security chính là "userId" của chúng ta

        return userRepository.findById(userId)
                // Chuyển Entity User (của bạn) thành UserDetails (của Spring)
                .map(userEntity -> new org.springframework.security.core.userdetails.User(
                        userEntity.getUserId(),      // Username (chúng ta dùng ID)
                        userEntity.getPasswordHash(),// Password (Spring cần)
                        Collections.emptyList()      // Quyền (Roles), chưa dùng
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với ID: " + userId));
    }
}