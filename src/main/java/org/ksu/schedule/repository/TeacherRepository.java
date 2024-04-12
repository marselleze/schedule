package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Teacher findByName(String nameTeacher);

    List<Teacher> findByPost(String post);

    void deleteByName(String nameTeacher);

    void deleteByPost(String post);


}
