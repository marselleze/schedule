package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.repository.GroupRepository;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubgroupDto {
    private int id;

    private String number;

    private GroupDto groupDto;


    public static SubgroupDto toDto(Subgroup subgroup) {
        return new SubgroupDto(
                subgroup.getId(),
                subgroup.getNumber(),
                GroupDto.toDto(subgroup.getGroup())
        );
    }



}
