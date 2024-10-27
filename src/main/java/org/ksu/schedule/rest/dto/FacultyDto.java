package org.ksu.schedule.rest.dto;

import lombok.*;
import org.ksu.schedule.domain.Faculty;

/**
 * DTO for {@link org.ksu.schedule.domain.Faculty}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacultyDto {
    Integer id;
    String facultyName;
    String abbreviation;

    public static FacultyDto toDto(Faculty faculty) {
        return FacultyDto.builder()
                .id(faculty.getId())
                .facultyName(faculty.getFacultyName())
                .abbreviation(faculty.getAbbreviation())
                .build();
    }

    public static Faculty toEntity(FacultyDto facultyDto) {
        return new Faculty(
                facultyDto.getId(),
                facultyDto.getFacultyName(),
                facultyDto.getAbbreviation()
        );
    }
}