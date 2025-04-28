package org.ksu.schedule.service;

import org.ksu.schedule.domain.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty getFacultyById(int id);

    Faculty addFaculty(Faculty faculty);

    Faculty updateFaculty(int id, String fullName, String abbreviation);

    List<Faculty> getAllFaculty();

    void deleteFaculty(int id);

    Faculty getFacultyByFacultyName(String facultyName);

    Faculty getFacultyByAbbreviation(String abbreviation);
}
