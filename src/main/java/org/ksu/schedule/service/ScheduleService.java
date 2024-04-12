package org.ksu.schedule.service;

import org.ksu.schedule.domain.Schedule;

import java.util.List;

public interface ScheduleService {



    List<Schedule> getBySubgroupId(int subgroup_id);

    List<Schedule> getByTeacherId(int teacher_id);

    List<Schedule> getByParity(String parity);

    void deleteById(int id);

    List<Schedule> getAll();

}
