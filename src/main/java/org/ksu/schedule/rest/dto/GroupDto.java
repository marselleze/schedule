package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<SubgroupDto> subgroups;

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

        List<SubgroupDto> subgroupDtos = group.getSubgroups() != null
                ? group.getSubgroups().stream()
                .map(SubgroupDto::toDto)
                .collect(Collectors.toList())
                : null;

        return GroupDto.builder()
                .id(group.getId())
                .number(group.getNumber())
                .direction(group.getDirection())
                .profile(group.getProfile())
                .subgroups(subgroupDtos)
                .build();
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

        List<Subgroup> subgroups = groupDto.getSubgroups() != null
                ? groupDto.getSubgroups().stream()
                .map(SubgroupDto::toDomain)
                .collect(Collectors.toList())
                : null;

        return new Group(
                groupDto.getId(),
                groupDto.getNumber(),
                groupDto.getDirection(),
                groupDto.getProfile(),
                subgroups
        );
    }
}
