package org.ksu.schedule.rest.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Role;

/**
 * Класс для регистрации пользователя.
 * Содержит данные, необходимые для создания нового пользователя.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * Имя пользователя.
     */
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    private String lastName;

    /**
     * Отчество пользователя.
     */
    private String middleName;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Номер группы пользователя.
     */
    private String group_number;

    /**
     * Номер подгруппы пользователя.
     */
    private String subgroup_number;

    /**
     * Дополнительная информация о пользователе.
     */
    private String info;

    /**
     * Роль пользователя.
     */
    private Role role;
}
