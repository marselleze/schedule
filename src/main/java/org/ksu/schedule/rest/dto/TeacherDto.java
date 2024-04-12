package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Teacher;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDto {

    private int id;

    private String name;

    private String post;

    public static TeacherDto toDto(Teacher teacher){
        return new TeacherDto(
                teacher.getId(),
                teacher.getName(),
                teacher.getPost()
        );
    }

    public static Teacher toDomain(TeacherDto teacherDto){
        return new Teacher(
                teacherDto.getId(),
                teacherDto.getName(),
                teacherDto.getPost()
        );
    }
}
