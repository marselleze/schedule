package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.*;
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
    public List<Schedule> getBySubgroupNumber(String subgroup_number) {
        Group group  = groupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null);
        if(group  == null){
            group = Group.builder()
                    .build();
        }

        Subgroup subgroup  = subgroupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null);
        if(subgroup  == null){
            subgroup = Subgroup.builder()
                    .number(subgroup_number)
                    .group(groupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null))
                    .build();
        }

        return scheduleRepository.findBySubgroupNumber(subgroup_number);
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
    public Schedule insert(int id, String parity, int subgroup_id, int teacher_id, int subject_id, String dayWeek, String timeStart, String timeEnd, String classroom) {
        Subgroup subgroup = subgroupRepository.findById(subgroup_id).orElse(null);

        Teacher teacher = teacherRepository.findById(teacher_id).orElse(null);

        Subject subject = subjectRepository.findById(subject_id).orElse(null);

        Schedule schedule = Schedule.builder()
                .id(id)
                .parity(parity)
                .subgroup(subgroup)
                .teacher(teacher)
                .subject(subject)
                .dayWeek(dayWeek)
                .timeStart(timeStart)
                .timeEnd(timeEnd)
                .classroom(classroom)
                .build();

        return scheduleRepository.saveAndFlush(schedule);
    }


    @Override
    public void deleteById(int id) {

        scheduleRepository.deleteById(id);

    }

    @Override
    public void deleteBySubgroupId(int subgroup_id) {

        scheduleRepository.deleteBySubgroupId(subgroup_id);
    }

    @Override
    public void deleteByTeacherId(int teacher_id) {

        scheduleRepository.deleteByTeacherId(teacher_id);

    }

    @Override
    public void deleteBySubjectId(int subject_id) {

        scheduleRepository.deleteBySubjectId(subject_id);
    }

    @Override
    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> getByTeacherName(String teacherName) {
        return scheduleRepository.findByTeacherName(teacherName);
    }

    @Override
    public List<Schedule> getBySubjectName(String subjectName) {
        return scheduleRepository.findBySubjectName(subjectName);
    }

    @Override
    public List<Schedule> getBySubgroupNumberAndSubjectType(String subgroup_number, String type) {
        return scheduleRepository.findBySubgroupNumberAndSubjectType(subgroup_number, type);
    }

    @Override
    public List<Schedule> getBySubgroupNumberAndTeacherName(String subgroup_number, String teacherName) {
        return scheduleRepository.findBySubgroupNumberAndTeacherName(subgroup_number, teacherName);
    }

    @Override
    public List<Schedule> getBySubgroupNumberAndSubjectName(String subgroup_number, String subjectName) {
        return scheduleRepository.findBySubgroupNumberAndSubjectName(subgroup_number, subjectName);
    }


}
