package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    List<Schedule> findBySubgroupId(int subgroup_id);

    List<Schedule> findByTeacherId(int teacher_id);

    List<Schedule> findByParity(String parity);

    void deleteBySubjectId(int subject_id);

    void deleteByTeacherId(int teacher_id);

    void deleteBySubgroupId(int subgroup_id);

    //@Query("select t.id from Teacher t where t.name = :name")
    //Schedule findByTeacherName(@Param("id")int id);
}
