package org.ksu.schedule.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Класс, представляющий сущность пользователя в системе.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * Email пользователя.
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
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Фото пользователя.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    @JsonManagedReference
    private Photo photo;

    /**
     * Возвращает список прав, предоставленных пользователю.
     *
     * @return список прав
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Возвращает имя пользователя (email).
     *
     * @return email пользователя
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Проверяет, не истек ли срок действия учетной записи.
     *
     * @return true, если срок действия не истек
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись.
     *
     * @return true, если учетная запись не заблокирована
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     *
     * @return true, если срок действия учетных данных не истек
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включена ли учетная запись.
     *
     * @return true, если учетная запись включена
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }
}
