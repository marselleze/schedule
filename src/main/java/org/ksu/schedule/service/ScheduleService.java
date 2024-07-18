package org.ksu.schedule.service;

import org.ksu.schedule.domain.Schedule;

import java.util.List;

/**
 * Сервис для управления расписанием.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface ScheduleService {

    /**
     * Получить расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     * @return список расписаний
     */
    List<Schedule> getBySubgroupId(int subgroup_id);

    /**
     * Получить расписание по номеру подгруппы.
     *
     * @param subgroup_number номер подгруппы
     * @return список расписаний
     */
    List<Schedule> getBySubgroupNumber(String subgroup_number);

    /**
     * Получить расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     * @return список расписаний
     */
    List<Schedule> getByTeacherId(int teacher_id);

    /**
     * Получить расписание по четности недели.
     *
     * @param parity четность недели
     * @return список расписаний
     */
    List<Schedule> getByParity(String parity);

    /**
     * Вставить новое расписание.
     *
     * @param id идентификатор расписания
     * @param parity четность недели
     * @param subgroup_id идентификатор подгруппы
     * @param teacher_id идентификатор преподавателя
     * @param subject_id идентификатор предмета
     * @param dayWeek день недели
     * @param timeStart время начала
     * @param timeEnd время окончания
     * @param classroom аудитория
     * @return созданное расписание
     */
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

    /**
     * Удалить расписание по идентификатору.
     *
     * @param id идентификатор расписания
     */
    void deleteById(int id);

    /**
     * Удалить расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     */
    void deleteBySubgroupId(int subgroup_id);

    /**
     * Удалить расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     */
    void deleteByTeacherId(int teacher_id);

    /**
     * Удалить расписание по идентификатору предмета.
     *
     * @param subject_id идентификатор предмета
     */
    void deleteBySubjectId(int subject_id);

    /**
     * Получить все расписания.
     *
     * @return список всех расписаний
     */
    List<Schedule> getAll();

    /**
     * Получить расписание по имени преподавателя.
     *
     * @param teacherName имя преподавателя
     * @return список расписаний
     */
    List<Schedule> getByTeacherName(String teacherName);

    /**
     * Получить расписание по названию предмета.
     *
     * @param subjectName название предмета
     * @return список расписаний
     */
    List<Schedule> getBySubjectName(String subjectName);

    /**
     * Получить расписание по номеру подгруппы и типу предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param type тип предмета
     * @return список расписаний
     */
    List<Schedule> getBySubgroupNumberAndSubjectType(String subgroup_number, String type);

    /**
     * Получить расписание по номеру подгруппы и имени преподавателя.
     *
     * @param subgroup_number номер подгруппы
     * @param teacherName имя преподавателя
     * @return список расписаний
     */
    List<Schedule> getBySubgroupNumberAndTeacherName(String subgroup_number, String teacherName);

    /**
     * Получить расписание по номеру подгруппы и названию предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param subjectName название предмета
     * @return список расписаний
     */
    List<Schedule> getBySubgroupNumberAndSubjectName(String subgroup_number, String subjectName);
}
