package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.rest.dto.ScheduleDto;
import org.ksu.schedule.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;


    @GetMapping("/schedule")
    public List<ScheduleDto> getAllSchedules() {
        return scheduleService.getAll()
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());

    }

    @GetMapping("/schedule/subgroup/{subgroup_id}")
    public List<ScheduleDto> getSchedulesBySubgroup(@PathVariable int subgroup_id) {

        return scheduleService.getBySubgroupId(subgroup_id)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule/teacher/{teacher_id}")
    public List<ScheduleDto> getSchedulesByTeacher(@PathVariable int teacher_id) {

        return scheduleService.getByTeacherId(teacher_id)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/schedule/parity/{parity}")
    public List<ScheduleDto> getSchedulesByParity(@PathVariable String parity) {

        return scheduleService.getByParity(parity)
                .stream()
                .map(ScheduleDto::toDto)
                .collect(Collectors.toList());
    }

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
