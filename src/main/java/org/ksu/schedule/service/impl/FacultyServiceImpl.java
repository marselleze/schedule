package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.service.FacultyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    @Override
    public Faculty getFacultyById(int id) {
        return facultyRepository.findById(id);
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty updateFaculty(int id, String fullName, String abbreviation) {
        Faculty faculty = Faculty.builder()
                .id(id)
                .facultyName(fullName)
                .abbreviation(abbreviation)
                .build();

        return facultyRepository.saveAndFlush(faculty);
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    @Override
    public void deleteFaculty(int id) {
        facultyRepository.deleteById(id);
    }

    @Override
    public Faculty getFacultyByFacultyName(String facultyName) {
        return facultyRepository.findByFacultyName(facultyName);
    }

    @Override
    public Faculty getFacultyByAbbreviation(String abbreviation) {
        return facultyRepository.findByAbbreviation(abbreviation);
    }
}
