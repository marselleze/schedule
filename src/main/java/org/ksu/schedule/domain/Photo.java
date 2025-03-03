package org.ksu.schedule.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, представляющий сущность фотографии в системе расписания.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Entity(name = "Photo")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Photo {

    /**
     * Уникальный идентификатор фотографии.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * URL фотографии.
     */
    private String url;

    @Override
    public String toString() {
        return "Photo{id=" + id + ", url='" + url + "'}";
    }
}
