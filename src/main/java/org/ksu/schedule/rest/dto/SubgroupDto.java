package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    public static Subgroup toDomain(SubgroupDto subgroupDto) {
        return new Subgroup(
                subgroupDto.getId(),
                subgroupDto.getNumber(),
                GroupDto.toDomain(subgroupDto.getGroupDto())
        );
    }



}
