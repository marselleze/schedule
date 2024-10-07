package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.rest.dto.SubjectDto;
import org.ksu.schedule.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления предметами.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubjectController {

    private final SubjectService subjectService;

    /**
     * Получить все предметы.
     *
     * @return список всех предметов в виде DTO.
     */
    @GetMapping("/subject")
    public List<SubjectDto> getAllSubjects() {
        return subjectService.getAll()
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Вставить новый предмет.
     *
     * @param subjectDto DTO предмета.
     * @return DTO нового предмета.
     */
    @PostMapping("/subject")
    public SubjectDto insertSubject(@RequestBody SubjectDto subjectDto) {
        Subject subject = subjectService.insert(SubjectDto.toDomain(subjectDto));
        return SubjectDto.toDto(subject);
    }

    /**
     * Обновить предмет.
     *
     * @param id идентификатор предмета.
     * @param name название предмета.
     * @param type тип предмета.
     * @return DTO обновленного предмета.
     */
    @PutMapping("/subject/{id}")
    public SubjectDto updateSubject(@PathVariable int id, @RequestParam String name, @RequestParam String type) {
        Subject subject = subjectService.update(id, name, type);
        return SubjectDto.toDto(subject);
    }

    /**
     * Удалить предмет по идентификатору.
     *
     * @param id идентификатор предмета.
     */
    @DeleteMapping("/subject/{id}")
    public void deleteSubject(@PathVariable int id) {
        subjectService.deleteById(id);
    }

    /**
     * Получить предметы по названию.
     *
     * @param name название предмета.
     * @return список предметов в виде DTO.
     */
    @GetMapping("/subject/name/{name}")
    public List<SubjectDto> getSubjectByName(@PathVariable String name) {
        return subjectService.getByName(name)
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получить предметы по типу.
     *
     * @param type тип предмета.
     * @return список предметов в виде DTO.
     */
    @GetMapping("/subject/type/{type}")
    public List<SubjectDto> getSubjectByType(@PathVariable String type) {
        return subjectService.getByType(type)
                .stream()
                .map(SubjectDto::toDto)
                .collect(Collectors.toList());
    }
}
