package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findByName(String nameSubject);

    List<Subject> findByType(String type);

    void deleteByName(String nameSubject);

    void deleteByType(String type);
}
