package com.testing.demo.filter;

import com.testing.demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Đăng ký "người gác cổng" này với Spring
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Sẽ tiêm (inject) ApplicationUserDetailsService

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        // 1. Kiểm tra xem có Header "Authorization" không và có bắt đầu bằng "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Nếu không có, cho qua (để đến /login)
            return;
        }

        // 2. Tách lấy token (sau chữ "Bearer ")
        jwt = authHeader.substring(7);

        try {
            // 3. Trích xuất userId từ token
            userId = jwtService.extractUserId(jwt);

            // 4. Nếu có userId VÀ user chưa được xác thực
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Tải thông tin User từ CSDL
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

                // 6. Kiểm tra xem token có hợp lệ không
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 7. Nếu hợp lệ, tạo "vé xác thực"
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Không cần credentials
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 8. Đặt "vé" này vào SecurityContext (Đánh dấu là user này đã được xác thực)
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Nếu token hết hạn, sai chữ ký, v.v.
            // Chúng ta không làm gì cả, user sẽ vẫn là "ẩn danh"
            System.out.println("[JWT Filter Error] " + e.getMessage());
        }

        // 9. Chuyển request cho bộ lọc tiếp theo
        filterChain.doFilter(request, response);
    }
}