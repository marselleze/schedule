package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO класс для представления подгруппы.
 *
 * @version 1.1
 * @author Егор Гришанов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubgroupDto {

    private int id;
    private String number;
    private GroupDto groupDto;
    private List<ScheduleDto> schedules;

    /**
     * Преобразует сущность {@link Subgroup} в DTO {@link SubgroupDto}.
     *
     * @param subgroup объект подгруппы
     * @return DTO объект подгруппы
     */
    public static SubgroupDto toDto(Subgroup subgroup) {
        return SubgroupDto.builder()
                .id(subgroup.getId())
                .number(subgroup.getNumber())
                .groupDto(GroupDto.toDto(subgroup.getGroup()))
                .schedules(subgroup.getSchedules() != null ?
                        subgroup.getSchedules().stream()
                                .map(ScheduleDto::toDto)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }

    /**
     * Преобразует DTO {@link SubgroupDto} в сущность {@link Subgroup}.
     *
     * @param subgroupDto DTO объект подгруппы
     * @return сущность подгруппы
     */
    public static Subgroup toDomain(SubgroupDto subgroupDto) {
        return Subgroup.builder()
                .id(subgroupDto.getId())
                .number(subgroupDto.getNumber())
                .group(GroupDto.toDomain(subgroupDto.getGroupDto()))
                .schedules(subgroupDto.getSchedules() != null ?
                        subgroupDto.getSchedules().stream()
                                .map(ScheduleDto::toDomain)
                                .collect(Collectors.toList())
                        : null)
                .build();
    }
}
