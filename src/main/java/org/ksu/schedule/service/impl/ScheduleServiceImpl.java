package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.*;
import org.ksu.schedule.repository.*;
import org.ksu.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация интерфейса {@link ScheduleService}.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final SubgroupRepository subgroupRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    /**
     * Получает расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubgroupId(int subgroup_id) {
        Group group = groupRepository.findById(subgroup_id).orElse(null);

        if (group == null) {
            group = Group.builder().build();
        }

        Subgroup subgroup = subgroupRepository.findById(subgroup_id).orElse(null);
        if (subgroup == null) {
            subgroup = Subgroup.builder()
                    .id(subgroup_id)
                    .group(groupRepository.findById(subgroup_id).orElse(null))
                    .build();
        }

        return scheduleRepository.findBySubgroupId(subgroup_id);
    }

    /**
     * Получает расписание по номеру подгруппы.
     *
     * @param subgroup_number номер подгруппы
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubgroupNumber(String subgroup_number) {
        Group group = groupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null);
        if (group == null) {
            group = Group.builder().build();
        }

        Subgroup subgroup = subgroupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null);
        if (subgroup == null) {
            subgroup = Subgroup.builder()
                    .number(subgroup_number)
                    .group(groupRepository.findById(subgroupRepository.findByNumber(subgroup_number).getId()).orElse(null))
                    .build();
        }

        return scheduleRepository.findBySubgroupNumber(subgroup_number);
    }

    /**
     * Получает расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     * @return список расписаний
     */
    @Override
    public List<Schedule> getByTeacherId(int teacher_id) {
        return scheduleRepository.findByTeacherId(teacher_id);
    }

    /**
     * Получает расписание по типу недели.
     *
     * @param parity тип недели
     * @return список расписаний
     */
    @Override
    public List<Schedule> getByParity(String parity) {
        return scheduleRepository.findByParity(parity);
    }

    /**
     * Вставляет новое расписание.
     *
     * @param id идентификатор расписания
     * @param parity тип недели
     * @param subgroup_id идентификатор подгруппы
     * @param teacher_id идентификатор преподавателя
     * @param subject_id идентификатор предмета
     * @param dayWeek день недели
     * @param timeStart время начала
     * @param timeEnd время окончания
     * @param classroom аудитория
     * @return сохраненное расписание
     */
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

    /**
     * Удаляет расписание по идентификатору.
     *
     * @param id идентификатор расписания
     */
    @Override
    public void deleteById(int id) {
        scheduleRepository.deleteById(id);
    }

    /**
     * Удаляет расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     */
    @Override
    public void deleteBySubgroupId(int subgroup_id) {
        scheduleRepository.deleteBySubgroupId(subgroup_id);
    }

    /**
     * Удаляет расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     */
    @Override
    public void deleteByTeacherId(int teacher_id) {
        scheduleRepository.deleteByTeacherId(teacher_id);
    }

    /**
     * Удаляет расписание по идентификатору предмета.
     *
     * @param subject_id идентификатор предмета
     */
    @Override
    public void deleteBySubjectId(int subject_id) {
        scheduleRepository.deleteBySubjectId(subject_id);
    }

    /**
     * Получает все расписания.
     *
     * @return список расписаний
     */
    @Override
    public List<Schedule> getAll() {
        return scheduleRepository.findAll();
    }

    /**
     * Получает расписание по имени преподавателя.
     *
     * @param teacherName имя преподавателя
     * @return список расписаний
     */
    @Override
    public List<Schedule> getByTeacherName(String teacherName) {
        return scheduleRepository.findByTeacherName(teacherName);
    }

    /**
     * Получает расписание по имени предмета.
     *
     * @param subjectName имя предмета
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubjectName(String subjectName) {
        return scheduleRepository.findBySubjectName(subjectName);
    }

    /**
     * Получает расписание по номеру подгруппы и типу предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param type тип предмета
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubgroupNumberAndSubjectType(String subgroup_number, String type) {
        return scheduleRepository.findBySubgroupNumberAndSubjectType(subgroup_number, type);
    }

    /**
     * Получает расписание по номеру подгруппы и имени преподавателя.
     *
     * @param subgroup_number номер подгруппы
     * @param teacherName имя преподавателя
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubgroupNumberAndTeacherName(String subgroup_number, String teacherName) {
        return scheduleRepository.findBySubgroupNumberAndTeacherName(subgroup_number, teacherName);
    }

    /**
     * Получает расписание по номеру подгруппы и имени предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param subjectName имя предмета
     * @return список расписаний
     */
    @Override
    public List<Schedule> getBySubgroupNumberAndSubjectName(String subgroup_number, String subjectName) {
        return scheduleRepository.findBySubgroupNumberAndSubjectName(subgroup_number, subjectName);
    }
}
