package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Group;

/**
 * DTO класс для представления группы.
 *
 * @version 1.0
 * @autor Егор Гришанов
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

    /**
     * Преобразует сущность {@link Group} в DTO {@link GroupDto}.
     *
     * @param group объект группы
     * @return DTO объект группы
     */
    public static GroupDto toDto(Group group) {
        return new GroupDto(
                group.getId(),
                group.getNumber(),
                group.getDirection(),
                group.getProfile()
        );
    }

    /**
     * Преобразует DTO {@link GroupDto} в сущность {@link Group}.
     *
     * @param groupDto DTO объект группы
     * @return сущность группы
     */
    public static Group toDomain(GroupDto groupDto) {
        return new Group(
                groupDto.getId(),
                groupDto.getNumber(),
                groupDto.getDirection(),
                groupDto.getProfile()
        );
    }
}
