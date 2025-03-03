package org.ksu.schedule.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksu.schedule.domain.Schedule;

/**
 * DTO класс для представления расписания.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
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
    private String classroom;

    /**
     * Преобразует сущность {@link Schedule} в DTO {@link ScheduleDto}.
     *
     * @param schedule объект расписания
     * @return DTO объект расписания
     */
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
                schedule.getClassroom()
        );
    }

    /**
     * Преобразует DTO {@link ScheduleDto} в сущность {@link Schedule}.
     *
     * @param scheduleDto DTO объект расписания
     * @return сущность расписания
     */
    public static Schedule toDomain(ScheduleDto scheduleDto) {
        return new Schedule(
                scheduleDto.getId(),
                scheduleDto.getParity(),
                SubgroupDto.toDomain(scheduleDto.getSubgroupDto()),
                SubjectDto.toDomain(scheduleDto.getSubjectDto()),
                TeacherDto.toDomain(scheduleDto.getTeacherDto()),
                scheduleDto.getDayWeek(),
                scheduleDto.getTimeStart(),
                scheduleDto.getTimeEnd(),
                scheduleDto.getClassroom()
        );
    }
}
