package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    Faculty findByFacultyName(String facultyName);

    Faculty findByAbbreviation(String abbreviation);

    Faculty findById(int id);

    List<Faculty> findAll ();

    boolean existsByFacultyName(String facultyName);

    void deleteByFacultyName(String facultyName);

    void deleteByAbbreviation(String abbreviation);

}
