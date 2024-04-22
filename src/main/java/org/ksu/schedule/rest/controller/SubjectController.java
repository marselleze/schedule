package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.rest.dto.SubjectDto;
import org.ksu.schedule.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/subject")
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAll()
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/subject")
    public SubjectDto insertSubject(@RequestBody SubjectDto subjectDto) {

        Subject subject = subjectService.insert(SubjectDto.toDomain(subjectDto));

        return SubjectDto.toDto(subject);
    }

    @PutMapping("/subject/{id}")
    public SubjectDto updateSubject(@PathVariable int id, @RequestParam String name, @RequestParam String type) {

        Subject subject = subjectService.update(id, name, type);

        return SubjectDto.toDto(subject);
    }

    @DeleteMapping("/subject/{id}")
    public void deleteSubject(@PathVariable int id) {
        subjectService.deleteById(id);
    }

    @GetMapping("/subject/name/{name}")
    public List<SubjectDto> getSubjectByName(@PathVariable String name) {

        return subjectService.getByName(name)
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());

    }

    @GetMapping("/subject/type/{type}")
    public List<SubjectDto> getSubjectByType(@PathVariable String type) {
        return subjectService.getByType(type)
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());
    }

}
