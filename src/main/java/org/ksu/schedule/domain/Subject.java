package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий сущность предмета в системе.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject")
public class Subject {

    /**
     * Уникальный идентификатор предмета.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Название предмета.
     */
    @Column(name = "name")
    private String name;

    /**
     * Тип предмета.
     */
    @Column(name = "type")
    private String type;
}
