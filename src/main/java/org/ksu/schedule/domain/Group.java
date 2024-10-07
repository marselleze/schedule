package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, представляющий сущность группы в системе расписания.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
public class Group {

    /**
     * Уникальный идентификатор группы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Номер группы.
     */
    @Column(name = "number")
    private String number;

    /**
     * Направление группы.
     */
    @Column(name = "direction")
    private String direction;

    /**
     * Профиль группы.
     */
    @Column(name = "profile")
    private String profile;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subgroup> subgroups;

}
