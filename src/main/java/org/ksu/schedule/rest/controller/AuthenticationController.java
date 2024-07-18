package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.config.AuthenticationService;
import org.ksu.schedule.config.JwtAuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtAuthenticationService jwtService;

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос с данными для регистрации
     * @return ответ с токеном аутентификации
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос с данными для аутентификации
     * @return ответ с токеном аутентификации
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    /**
     * Обновление JWT токена.
     *
     * @param token старый JWT токен
     * @return ответ с новым JWT токеном
     */
    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            String newToken = jwtService.refreshToken(token);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + newToken);
            return ResponseEntity.ok().headers(headers).body("Token refreshed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
