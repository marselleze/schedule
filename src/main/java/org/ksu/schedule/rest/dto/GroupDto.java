package org.ksu.schedule.rest.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO класс для представления группы.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {

    private int id;
    private String number;
    private String direction;
    private String profile;

    private FacultyDto faculty;


    /**
     * Преобразует сущность {@link Group} в DTO {@link GroupDto}.
     *
     * @param group объект группы
     * @return DTO объект группы
     */
    public static GroupDto toDto(Group group) {
        if (group == null) {
            return null;
        }


        return new GroupDto(
                group.getId(),
                group.getNumber(),
                group.getDirection(),
                group.getProfile(),
                FacultyDto.toDto(group.getFaculty())
        );
    }

    /**
     * Преобразует DTO {@link GroupDto} в сущность {@link Group}.
     *
     * @param groupDto DTO объект группы
     * @return сущность группы
     */
    public static Group toDomain(GroupDto groupDto) {
        if (groupDto == null) {
            return null;
        }

        return new Group(
                groupDto.getId(),
                groupDto.getNumber(),
                groupDto.getDirection(),
                groupDto.getProfile(),
                FacultyDto.toEntity(groupDto.getFaculty())
        );
    }
}
