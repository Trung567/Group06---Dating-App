package com.testing.demo.service;

import com.testing.demo.dto.PreferenceDto;
import com.testing.demo.entity.Preference;
import com.testing.demo.entity.User;
import com.testing.demo.repository.PreferenceRepository;
import com.testing.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy Preference của user
    public PreferenceDto getMyPreference(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tìm preference trong DB, nếu chưa có thì trả về mặc định
        Preference preference = preferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    // Trả về giá trị mặc định nếu người dùng chưa cài đặt
                    Preference defaultPref = new Preference();
                    defaultPref.setMinAge(18);
                    defaultPref.setMaxAge(50);
                    defaultPref.setMaxDistance(50);
                    defaultPref.setInterestedInGender("BOTH");
                    return defaultPref;
                });

        return mapToDto(preference);
    }

    // Cập nhật Preference
    public PreferenceDto updateMyPreference(String email, PreferenceDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Tìm cái cũ để update, hoặc tạo mới nếu chưa có
        Preference preference = preferenceRepository.findByUser(user)
                .orElse(new Preference());

        // Gán User cho preference nếu là tạo mới
        if (preference.getUser() == null) {
            preference.setUser(user);
        }

        // Cập nhật thông tin từ DTO
        preference.setMinAge(dto.getMinAge());
        preference.setMaxAge(dto.getMaxAge());
        preference.setInterestedInGender(dto.getInterestedInGender());
        preference.setMaxDistance(dto.getMaxDistance());

        // Lưu vào DB
        Preference savedPref = preferenceRepository.save(preference);

        return mapToDto(savedPref);
    }

    // Helper method để chuyển Entity -> DTO
    private PreferenceDto mapToDto(Preference entity) {
        return new PreferenceDto(
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getInterestedInGender(),
                entity.getMaxDistance()
        );
    }
}