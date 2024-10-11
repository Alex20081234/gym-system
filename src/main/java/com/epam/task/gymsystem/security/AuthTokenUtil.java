package com.epam.task.gymsystem.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Base64;

public class AuthTokenUtil {

    private AuthTokenUtil() {}

    public static String generateToken(String username, String password) {
        String token = username + ":" + password;
        return Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isValidToken(String token, String username, String password) {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedToken = new String(decodedBytes, StandardCharsets.UTF_8);
        String expectedToken = username + ":" + password;
        return decodedToken.equals(expectedToken);
    }

    public static void validateToken(String username, String password, String token) throws AccessDeniedException {
        if (token == null) {
            throw new AccessDeniedException("Unauthorized access");
        }
        if (!isValidToken(token, username, password)) {
            throw new AccessDeniedException("Invalid token");
        }
    }

    public static void verifyCookie(HttpServletRequest request, String username, String password) throws AccessDeniedException {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AccessDeniedException("Unauthorized access");
        }
        String token = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("authCookie")) {
                token = cookie.getValue();
                break;
            }
        }
        AuthTokenUtil.validateToken(username, password, token);
    }
}
