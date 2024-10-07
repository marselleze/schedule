package org.ksu.schedule.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Конфигурация безопасности приложения.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Autowired
    private DataSource dataSource;

    /**
     * Конфигурация цепочки фильтров безопасности.
     *
     * @param http объект {@link HttpSecurity} для конфигурации безопасности
     * @return объект {@link SecurityFilterChain}, содержащий цепочку фильтров безопасности
     * @throws Exception если возникает ошибка конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/schedule/subgroup/**").hasAuthority("STUDENT")
                .requestMatchers("/api/v1/schedule/**").hasAuthority("TEACHER")
                .requestMatchers("/api/v1/import/import-excel").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/user/update/student/").hasAuthority("STUDENT")
                .requestMatchers("/api/v1/user/update/teacher/").hasAuthority("TEACHER")
                .anyRequest().permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .rememberMe()
                .key(secretKey)
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(14 * 24 * 60  * 60)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Репозиторий для хранения токенов "Запомнить меня".
     *
     * @return объект {@link PersistentTokenRepository}, используемый для хранения токенов "Запомнить меня"
     */
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }
}
