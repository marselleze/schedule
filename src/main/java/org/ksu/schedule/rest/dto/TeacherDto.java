package org.ksu.schedule.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Photo;
import org.ksu.schedule.domain.Teacher;

/**
 * DTO класс для представления преподавателя.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {

    private int id;
    private String name;
    private String post;


    /**
     * Преобразует сущность {@link Teacher} в DTO {@link TeacherDto}.
     *
     * @param teacher объект преподавателя
     * @return DTO объект преподавателя
     */
    public static TeacherDto toDto(Teacher teacher) {
        return new TeacherDto(
                teacher.getId(),
                teacher.getName(),
                teacher.getPost());
    }

    /**
     * Преобразует DTO {@link TeacherDto} в сущность {@link Teacher}.
     *
     * @param teacherDto DTO объект преподавателя
     * @return сущность преподавателя
     */
    public static Teacher toDomain(TeacherDto teacherDto) {
        return new Teacher(
                teacherDto.getId(),
                teacherDto.getName(),
                teacherDto.getPost()
        );
    }
}
