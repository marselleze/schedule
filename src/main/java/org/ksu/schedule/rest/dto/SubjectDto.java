package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Subject;

/**
 * DTO класс для представления предмета.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {

    private int id;
    private String name;
    private String type;

    /**
     * Преобразует сущность {@link Subject} в DTO {@link SubjectDto}.
     *
     * @param subject объект предмета
     * @return DTO объект предмета
     */
    public static SubjectDto toDto(Subject subject) {
        return new SubjectDto(
                subject.getId(),
                subject.getName(),
                subject.getType()
        );
    }

    /**
     * Преобразует DTO {@link SubjectDto} в сущность {@link Subject}.
     *
     * @param subjectDto DTO объект предмета
     * @return сущность предмета
     */
    public static Subject toDomain(SubjectDto subjectDto) {
        return new Subject(
                subjectDto.getId(),
                subjectDto.getName(),
                subjectDto.getType()
        );
    }
}
