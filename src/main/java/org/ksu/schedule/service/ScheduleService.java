package org.ksu.schedule.service;

import org.ksu.schedule.domain.Schedule;

import java.util.List;

public interface ScheduleService {



    List<Schedule> getBySubgroupId(int subgroup_id);

    List<Schedule> getByTeacherId(int teacher_id);

    List<Schedule> getByParity(String parity);

    Schedule insert(
            int id,
            String parity,
            int subgroup_id,
            int teacher_id,
            int subject_id,
            String dayWeek,
            String timeStart,
            String timeEnd,
            String classroom

    );

    void deleteById(int id);

    void deleteBySubgroupId(int subgroup_id);

    void deleteByTeacherId(int teacher_id);

    void deleteBySubjectId(int subject_id);

    List<Schedule> getAll();

}
