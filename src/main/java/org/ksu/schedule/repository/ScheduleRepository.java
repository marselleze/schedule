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

    List<Schedule> findBySubgroupNumber(String subgroup_number);

    List<Schedule> findByGroupNumber(String group_number);

    List<Schedule> findByTeacherId(int teacher_id);

    List<Schedule> findByTeacherName(String teacher_name);

    List<Schedule> findByParity(String parity);

    List<Schedule> findBySubjectName(String subject_name);


    void deleteBySubjectId(int subject_id);

    void deleteByTeacherId(int teacher_id);

    void deleteBySubgroupId(int subgroup_id);

    List<Schedule> findBySubgroupNumberAndSubjectType(String subgroup_number, String subject_type);

    List<Schedule> findBySubgroupNumberAndTeacherName(String subgroup_number, String teacher_name);

    List<Schedule> findBySubgroupNumberAndSubjectName(String subgroup_number, String subject_name);
    //@Query("select t.id from Teacher t where t.name = :name")
    //Schedule findByTeacherName(@Param("id")int id);
}
