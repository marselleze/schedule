package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Schedule;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.repository.*;
import org.ksu.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final SubgroupRepository subgroupRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;


    @Override
    public List<Schedule> getBySubgroupId(int subgroup_id) {

        Group group = groupRepository.findById(subgroup_id).orElse(null);

        if(group == null){

            group = Group.builder()
                    .build();
        }

        Subgroup subgroup = subgroupRepository.findById(subgroup_id).orElse(null);
        if(subgroup == null){

            subgroup = Subgroup.builder()
                    .id(subgroup_id)
                    .group(groupRepository.findById(subgroup_id).orElse(null))
                    .build();
        }


        return scheduleRepository.findBySubgroupId(subgroup_id);
    }

    @Override
    public List<Schedule> getByTeacherId(int teacher_id) {

        return scheduleRepository.findByTeacherId(teacher_id);
    }

    @Override
    public List<Schedule> getByParity(String parity) {
        return scheduleRepository.findByParity(parity);
    }


    @Override
    public void deleteById(int id) {

        scheduleRepository.deleteById(id);

    }

    @Override
    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }
}
