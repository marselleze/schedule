package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.rest.dto.TeacherDto;
import org.ksu.schedule.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления преподавателями.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TeacherController {

    private final TeacherService teacherService;

    /**
     * Получить всех преподавателей.
     *
     * @return список всех преподавателей в виде DTO.
     */
    @GetMapping("/teacher")
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getAll()
                .stream()
                .map(TeacherDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Вставить нового преподавателя.
     *
     * @param teacherDto DTO преподавателя.
     * @return DTO нового преподавателя.
     */
    @PostMapping("/teacher")
    public TeacherDto insertTeacher(@RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherService.insert(TeacherDto.toDomain(teacherDto));
        return TeacherDto.toDto(teacher);
    }

    /**
     * Обновить данные преподавателя.
     *
     * @param id идентификатор преподавателя.
     * @param name имя преподавателя.
     * @param post должность преподавателя.
     * @return DTO обновленного преподавателя.
     */
    @PutMapping("/teacher/{id}")
    public TeacherDto updateTeacher(@PathVariable int id, @RequestParam String name, @RequestParam String post) {
        Teacher teacher = teacherService.update(id, name, post);
        return TeacherDto.toDto(teacher);
    }

    /**
     * Удалить преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя.
     */
    @DeleteMapping("/teacher/{id}")
    public void deleteTeacher(@PathVariable int id) {
        teacherService.deleteById(id);
    }

    /**
     * Получить преподавателей по должности.
     *
     * @param post должность преподавателя.
     * @return список преподавателей в виде DTO.
     */
    @GetMapping("/teacher/post/{post}")
    public List<TeacherDto> getTeachersByPost(@PathVariable String post) {
        return teacherService.getByPost(post)
                .stream()
                .map(TeacherDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить преподавателя по имени.
     *
     * @param name имя преподавателя.
     * @return DTO преподавателя.
     */
    @GetMapping("/teacher/name/{name}")
    public TeacherDto getTeachersByName(@PathVariable String name) {
        Teacher teacher = teacherService.getByName(name);
        return TeacherDto.toDto(teacher);
    }

    /**
     * Получить преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя.
     * @return DTO преподавателя.
     */
    @GetMapping("/teacher/id/{id}")
    public TeacherDto getTeacherById(@PathVariable int id) {
        Teacher teacher = teacherService.getById(id);
        return TeacherDto.toDto(teacher);
    }
}
