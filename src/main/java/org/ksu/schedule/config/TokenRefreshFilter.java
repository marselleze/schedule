package org.ksu.schedule.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для обновления токенов при их истечении.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Component
@RequiredArgsConstructor
public class TokenRefreshFilter extends OncePerRequestFilter {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserDetailsService userDetailsService;

    /**
     * Метод для выполнения внутренней фильтрации.
     *
     * @param request     HTTP-запрос
     * @param response    HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            try {
                jwtAuthenticationService.extractEmail(token);
            } catch (ExpiredJwtException e) {
                // Обработка логики обновления токена
                String newToken = jwtAuthenticationService.refreshToken(token);
                response.setHeader("Authorization", "Bearer " + newToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
