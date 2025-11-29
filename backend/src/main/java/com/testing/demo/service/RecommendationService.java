package com.testing.demo.service;


import com.testing.demo.entity.Preference;
import com.testing.demo.entity.User;
import com.testing.demo.repository.BlockRepo;
import com.testing.demo.repository.PreferenceRepository;
import com.testing.demo.repository.SwipeRepository;
import com.testing.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PreferenceRepository preferenceRepository;
    @Autowired
    private SwipeRepository swipeRepository;
    @Autowired
    private BlockRepo blockRepository;
    // Hàm chính: Lấy gợi ý
    public List<User> getRecommendations(String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 1. Lấy Preference (Nếu không có, dùng mặc định)
        Preference pref = preferenceRepository.findByUser(currentUser)
                .orElseGet(() -> { // Dùng .orElseGet() để hiệu năng tốt hơn
                    Preference defaultPref = new Preference(); // Dùng constructor rỗng
                    defaultPref.setMinAge(18);
                    defaultPref.setMaxAge(100);
                    defaultPref.setInterestedInGender("BOTH");
                    defaultPref.setMaxDistance(100);
                    return defaultPref;
                });

        // 2. (V4) Lấy danh sách ID đã quẹt để loại trừ
        List<String> swipedIds = swipeRepository.findSwipedUserIds(currentUser.getId());
        // Thêm chính user hiện tại vào danh sách loại trừ
        List<String> blockedIds = blockRepository.findBlockedUserIds(currentUser.getId());
        swipedIds.addAll(blockedIds);
        List<String> blockerIds = blockRepository.findBlockerUserIds(currentUser.getId());
        swipedIds.addAll(blockerIds);

        swipedIds.add(currentUser.getId());

        // 3. Lấy tất cả user CHƯA QUẸT từ DB (Tối ưu bước đầu)
        // (Lưu ý: Nếu danh sách swipedIds rỗng, cần xử lý để tránh lỗi SQL IN empty)
        List<User> candidates;
        if (swipedIds.isEmpty()) {
            candidates = userRepository.findAll();
        } else {
            // Bạn cần thêm hàm findAllByIdNotIn trong UserRepository
            candidates = userRepository.findAllByIdNotIn(swipedIds);
        }

        // 4. Lọc mềm (Filtering in Memory) cho V2 (Tuổi, Giới tính) và V3 (Khoảng cách)
        return candidates.stream()
                .filter(user -> isGenderMatch(user, pref.getInterestedInGender())) // Lọc giới tính
                .filter(user -> isAgeMatch(user, pref.getMinAge(), pref.getMaxAge())) // Lọc tuổi
                .filter(user -> isDistanceMatch(currentUser, user, pref.getMaxDistance())) // Lọc khoảng cách
                .collect(Collectors.toList());
    }

    // --- CÁC HÀM HỖ TRỢ ---

    // V2: Logic lọc giới tính
    private boolean isGenderMatch(User target, String interest) {
        if ("BOTH".equalsIgnoreCase(interest)) return true;
        // Giả sử User có field 'gender'. Cần đảm bảo null check.
        return target.getGender() != null && target.getGender().equalsIgnoreCase(interest);
    }

    // V2: Logic lọc tuổi
    private boolean isAgeMatch(User target, int min, int max) {
        if (target.getBirthDate() == null) return false; // Không có ngày sinh thì bỏ qua
        int age = Period.between(target.getBirthDate(), LocalDate.now()).getYears();
        return age >= min && age <= max;
    }

    // V3: Logic lọc khoảng cách (Haversine Formula)
    private boolean isDistanceMatch(User current, User target, int maxDistance) {
        if (current.getLatitude() == null || current.getLongitude() == null ||
                target.getLatitude() == null || target.getLongitude() == null) {
            return false; // Không có vị trí thì không tính được -> bỏ qua
        }

        double distance = calculateHaversineDistance(
                current.getLatitude(), current.getLongitude(),
                target.getLatitude(), target.getLongitude()
        );

        return distance <= maxDistance;
    }

    // Công thức tính khoảng cách (đơn vị km)
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}