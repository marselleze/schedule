package org.ksu.schedule.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.domain.Teacher;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {

    private int id;

    private String parity;

    private SubgroupDto subgroupDto;

    private SubjectDto subjectDto;

    private TeacherDto teacherDto;

    private String dayWeek;

    private String timeStart;

    private String timeEnd;

    private int number;

    private String classroom;






    public static ScheduleDto toDto(Schedule schedule) {
        return new ScheduleDto(
                schedule.getId(),
                schedule.getParity(),
                SubgroupDto.toDto(schedule.getSubgroup()),
                SubjectDto.toDto(schedule.getSubject()),
                TeacherDto.toDto(schedule.getTeacher()),
                schedule.getDayWeek(),
                schedule.getTimeStart(),
                schedule.getTimeEnd(),
                schedule.getNumber(),
                schedule.getClassroom()
        );


    }


}
