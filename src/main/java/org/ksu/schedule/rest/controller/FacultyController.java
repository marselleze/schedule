package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.rest.dto.FacultyDto;
import org.ksu.schedule.service.FacultyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FacultyController {
    private final FacultyService facultyService;

    @GetMapping("/faculty")
    public List<FacultyDto> getAllFaculty() {
        return facultyService.getAllFaculty().stream().map(FacultyDto::toDto).collect(Collectors.toList());
    }

    @GetMapping("/faculty/name/{name}")
    public FacultyDto getFacultyByName(@PathVariable String name) {
        Faculty faculty = facultyService.getFacultyByFacultyName(name);
        return FacultyDto.toDto(faculty);
    }

    @GetMapping("/faculty/abbreviation/{abbreviation}")
    public FacultyDto getFacultyByAbbreviation(@PathVariable String abbreviation) {
        Faculty faculty = facultyService.getFacultyByAbbreviation(abbreviation);
        return FacultyDto.toDto(faculty);
    }

    @GetMapping("/faculty/{id}")
    public FacultyDto getFacultyById(@PathVariable int id) {
        Faculty faculty = facultyService.getFacultyById(id);
        return FacultyDto.toDto(faculty);
    }

    @PostMapping("/faculty/add")
    public FacultyDto insert(@RequestBody Faculty faculty) {
        Faculty faculty1 = facultyService.addFaculty(faculty);
        return FacultyDto.toDto(faculty1);
    }

    @PutMapping("/faculty/{id}")
    public FacultyDto update(@PathVariable int id, @RequestBody Faculty faculty) {
        Faculty faculty1 = facultyService.updateFaculty(id, faculty.getFacultyName(), faculty.getAbbreviation());
        return FacultyDto.toDto(faculty1);
    }

    @DeleteMapping("/faculty/delete/{id}")
    public void delete(@PathVariable int id) {
        facultyService.deleteFaculty(id);
    }
}
