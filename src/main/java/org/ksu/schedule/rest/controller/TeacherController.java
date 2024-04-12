package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.rest.dto.TeacherDto;
import org.ksu.schedule.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/teachers")
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getAll()
                .stream()
                .map(TeacherDto::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/teachers")
    public TeacherDto insertTeacher(@RequestBody TeacherDto teacherDto) {

        Teacher teacher = teacherService.insert(TeacherDto.toDomain(teacherDto));

        return TeacherDto.toDto(teacher);
    }

    @PutMapping("/teachers/{id}")
    public TeacherDto updateTeacher(@PathVariable int id, @RequestParam String name, @RequestParam String post) {

        Teacher teacher = teacherService.update(id, name, post);

        return TeacherDto.toDto(teacher);
    }

    @DeleteMapping("/teachers/{id}")
    public void deleteTeacher(@PathVariable int id) {
        teacherService.deleteById(id);
    }

    @GetMapping("/teachers/post/{post}")
    public List<TeacherDto> getTeachersByPost(@PathVariable String post){

        return teacherService.getByPost(post)
                .stream()
                .map(TeacherDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/teachers/name/{name}")
    public TeacherDto getTeachersByName(@PathVariable String name){

        Teacher teacher = teacherService.getByName(name);

        return TeacherDto.toDto(teacher);
    }
}
