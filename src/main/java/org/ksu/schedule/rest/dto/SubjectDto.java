package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Subject;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {

    private int id;

    private String name;

    private String type;

    public static SubjectDto toDto(Subject subject){
        return new SubjectDto(
                subject.getId(),
                subject.getName(),
                subject.getType()
        );

    }

    public static Subject toDomain(SubjectDto subjectDto){
        return new Subject(
                subjectDto.getId(),
                subjectDto.getName(),
                subjectDto.getType()
        );
    }

}
