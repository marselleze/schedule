package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Subgroup;

/**
 * DTO класс для представления подгруппы.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubgroupDto {

    private int id;
    private String number;
    private GroupDto groupDto;

    /**
     * Преобразует сущность {@link Subgroup} в DTO {@link SubgroupDto}.
     *
     * @param subgroup объект подгруппы
     * @return DTO объект подгруппы
     */
    public static SubgroupDto toDto(Subgroup subgroup) {
        return new SubgroupDto(
                subgroup.getId(),
                subgroup.getNumber(),
                GroupDto.toDto(subgroup.getGroup())
        );
    }

    /**
     * Преобразует DTO {@link SubgroupDto} в сущность {@link Subgroup}.
     *
     * @param subgroupDto DTO объект подгруппы
     * @return сущность подгруппы
     */
    public static Subgroup toDomain(SubgroupDto subgroupDto) {
        return new Subgroup(
                subgroupDto.getId(),
                subgroupDto.getNumber(),
                GroupDto.toDomain(subgroupDto.getGroupDto())
        );
    }
}
