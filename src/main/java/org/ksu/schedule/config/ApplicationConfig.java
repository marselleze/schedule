package org.ksu.schedule.config;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс приложения для настройки аутентификации и шифрования паролей.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    /**
     * Определяет сервис, предоставляющий информацию о пользователях.
     *
     * @return реализация {@link UserDetailsService}, используемая для загрузки пользователей по их email
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Определяет провайдера аутентификации.
     *
     * @return реализация {@link AuthenticationProvider}, используемая для аутентификации пользователей
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Определяет менеджер аутентификации.
     *
     * @param config конфигурация аутентификации
     * @return реализация {@link AuthenticationManager}, используемая для аутентификации пользователей
     * @throws Exception если возникает ошибка при создании менеджера аутентификации
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Определяет шифровальщик паролей.
     *
     * @return реализация {@link PasswordEncoder}, используемая для шифрования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
