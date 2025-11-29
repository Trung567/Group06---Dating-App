package com.testing.demo.service;

import com.testing.demo.entity.User;
import io.jsonwebtoken.Claims; // Import
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails; // Import
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets; // Import
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function; // Import

@Service
public class JwtService {

    // 1. Đọc "secret key" từ file application.properties
    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    /**
     * Hàm tạo ra một token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("firstName", user.getFirstName());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 1000 * 60 * 60 * 24); // Hạn: 1 ngày

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUserId()) // Chủ thể của token (chính là User ID)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // ----- CÁC HÀM ĐỌC TOKEN (Sẽ dùng cho "Bảo vệ") -----
    // (Thêm các hàm này ngay bây giờ để chuẩn bị cho bước sau)

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return (userId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}