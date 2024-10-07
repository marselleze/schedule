package org.ksu.schedule.rest.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Запрос на аутентификацию.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    /**
     * Электронная почта пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;
}
