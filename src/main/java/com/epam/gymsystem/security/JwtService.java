package com.epam.gymsystem.security;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import com.epam.gymsystem.dao.BlacklistDao;
import com.epam.gymsystem.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Service
@AllArgsConstructor
public class JwtService {
    private final BlacklistDao dao;
    @Value("${gym-system.secretKey}")
    private final String key;
    @Value("${gym-system.jwtExpirationMs}")
    private final long jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        User userPrincipal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .subject((userPrincipal.getUsername()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(secretKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(secretKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey()).build().parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenBlackListed(String token) {
        return dao.isBlacklisted(token);
    }

    public void blacklistToken(String token) {
        Token current = Token.builder()
                .id(token)
                .expireDate(getExpireDateFromJwtToken(token))
                .build();
        dao.blacklist(current);
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, "HmacSHA256");
    }

    private LocalDate getExpireDateFromJwtToken(String token) {
        return Jwts.parser().verifyWith(secretKey()).build()
                .parseSignedClaims(token).getPayload().getExpiration()
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
