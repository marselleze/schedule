package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

/**
 * Перечисление, представляющее роли пользователей в системе расписания.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public enum Role {
    STUDENT,
    MONITOR,
    TEACHER,
    ADMIN
}
