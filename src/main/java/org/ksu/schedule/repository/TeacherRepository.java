package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Teacher findByName(String nameTeacher);

    @Query("SELECT DISTINCT s FROM Teacher s WHERE s.name = ?1 AND s.post =?2")
    Teacher findByNameAndPost(@Param("name") String nameTeacher, @Param ("post") String post);

    Teacher findOneById(int id);

    List<Teacher> findByPost(String post);

    void deleteByName(String nameTeacher);

    void deleteByPost(String post);


}
