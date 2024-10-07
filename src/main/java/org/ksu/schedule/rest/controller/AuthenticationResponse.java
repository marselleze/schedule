package org.ksu.schedule.rest.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ответ на запрос аутентификации.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    /**
     * JWT токен.
     */
    private String token;
}
