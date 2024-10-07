package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.rest.dto.ScheduleDto;
import org.ksu.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления расписанием.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Получить все расписания.
     *
     * @return список всех расписаний в виде DTO.
     */
    @GetMapping("/schedule")
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAll()
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/subgroup/id/{subgroup_id}")
    public List<ScheduleDto> getSchedulesBySubgroup(@PathVariable int subgroup_id) {
        return scheduleService.getBySubgroupId(subgroup_id)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по номеру подгруппы.
     *
     * @param subgroup_number номер подгруппы.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/subgroup/number/{subgroup_number}")
    public List<ScheduleDto> getSchedulesBySubgroupNumber(@PathVariable String subgroup_number) {
        return scheduleService.getBySubgroupNumber(subgroup_number)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/teacher/{teacher_id}")
    public List<ScheduleDto> getSchedulesByTeacher(@PathVariable int teacher_id) {
        return scheduleService.getByTeacherId(teacher_id)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по имени преподавателя.
     *
     * @param teacher_name имя преподавателя.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/teacher/name/{teacher_name}")
    public List<ScheduleDto> getSchedulesByTeacherName(@PathVariable String teacher_name) {
        return scheduleService.getByTeacherName(teacher_name)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по четности недели.
     *
     * @param parity четность недели (четная или нечетная).
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/parity/{parity}")
    public List<ScheduleDto> getSchedulesByParity(@PathVariable String parity) {
        return scheduleService.getByParity(parity)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по названию предмета.
     *
     * @param subject_name название предмета.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/subject/{subject_name}")
    public List<ScheduleDto> getSchedulesBySubject(@PathVariable String subject_name) {
        return scheduleService.getBySubjectName(subject_name)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по номеру подгруппы и типу предмета.
     *
     * @param subgroup_number номер подгруппы.
     * @param type тип предмета.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/filter/subgroup_number{subgroup_number}/type/{type}")
    public List<ScheduleDto> getSchedulesBySubgroupNumberAndType(@PathVariable("subgroup_number") String subgroup_number,
                                                                 @PathVariable("type") String type) {
        return scheduleService.getBySubgroupNumberAndSubjectType(subgroup_number, type)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по номеру подгруппы и имени преподавателя.
     *
     * @param subgroup_number номер подгруппы.
     * @param teacher_name имя преподавателя.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/filter/subgroup_number{subgroup_number}/teacher_name/{teacher_name}")
    public List<ScheduleDto> getSchedulesBySubgroupNumberAndTeacherName(@PathVariable("subgroup_number") String subgroup_number,
                                                                        @PathVariable("teacher_name") String teacher_name) {
        return scheduleService.getBySubgroupNumberAndSubjectName(subgroup_number, teacher_name)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить расписания по номеру подгруппы и названию предмета.
     *
     * @param subgroup_number номер подгруппы.
     * @param subject_name название предмета.
     * @return список расписаний в виде DTO.
     */
    @GetMapping("/schedule/filter/subgroup_number/{subgroup_number}/subject_name/{subject_name}")
    public List<ScheduleDto> getSchedulesBySubgroupNumberAndSubjectName(@PathVariable("subgroup_number") String subgroup_number,
                                                                        @PathVariable("subject_name") String subject_name) {
        return scheduleService.getBySubgroupNumberAndSubjectName(subgroup_number, subject_name)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Удалить расписание по идентификатору.
     *
     * @param id идентификатор расписания.
     */
    @DeleteMapping("/schedule/{id}")
    public void delete(@PathVariable int id) {
        scheduleService.deleteById(id);
    }

    /**
     * Вставить новое расписание.
     *
     * @param id идентификатор расписания.
     * @param parity четность недели.
     * @param subgroup_id идентификатор подгруппы.
     * @param subject_id идентификатор предмета.
     * @param teacher_id идентификатор преподавателя.
     * @param dayWeek день недели.
     * @param timeStart время начала.
     * @param timeEnd время окончания.
     * @param classroom аудитория.
     * @return DTO нового расписания.
     */
    @PostMapping("/schedule")
    public ScheduleDto insert(@RequestParam int id,
                              @RequestParam String parity,
                              @RequestParam int subgroup_id,
                              @RequestParam int subject_id,
                              @RequestParam int teacher_id,
                              @RequestParam String dayWeek,
                              @RequestParam String timeStart,
                              @RequestParam String timeEnd,
                              @RequestParam String classroom) {
        Schedule schedule = scheduleService.insert(id, parity, subgroup_id, subject_id, teacher_id, dayWeek, timeStart, timeEnd, classroom);
        return ScheduleDto.toDto(schedule);
    }
}
