package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDto {

    private int id;

    private int number;

    private String direction;

    private String profile;

    public static GroupDto toDto(Group group) {
        return new GroupDto(
                group.getId(),
                group.getNumber(),
                group.getDirection(),
                group.getProfile()

        );
    }

    public static Group toDomain(GroupDto groupDto) {
        return new Group(
                groupDto.getId(),
                groupDto.getNumber(),
                groupDto.getDirection(),
                groupDto.getProfile()
        );
    }
}
