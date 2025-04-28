package org.ksu.schedule.repository;

import jakarta.transaction.Transactional;
import org.ksu.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Schedule}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    /**
     * Находит расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     * @return список расписаний
     */
    List<Schedule> findBySubgroupId(int subgroup_id);

    /**
     * Находит расписание по номеру подгруппы.
     *
     * @param subgroup_number номер подгруппы
     * @return список расписаний
     */
    List<Schedule> findBySubgroupNumber(String subgroup_number);

    /**
     * Находит расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     * @return список расписаний
     */
    List<Schedule> findByTeacherId(int teacher_id);

    /**
     * Находит расписание по имени преподавателя.
     *
     * @param teacher_name имя преподавателя
     * @return список расписаний
     */
    List<Schedule> findByTeacherName(String teacher_name);

    /**
     * Находит расписание по четности недели.
     *
     * @param parity четность недели
     * @return список расписаний
     */
    List<Schedule> findByParity(String parity);

    /**
     * Находит расписание по названию предмета.
     *
     * @param subject_name название предмета
     * @return список расписаний
     */
    List<Schedule> findBySubjectName(String subject_name);

    /**
     * Удаляет расписание по идентификатору предмета.
     *
     * @param subject_id идентификатор предмета
     */
    void deleteBySubjectId(int subject_id);

    /**
     * Удаляет расписание по идентификатору преподавателя.
     *
     * @param teacher_id идентификатор преподавателя
     */
    void deleteByTeacherId(int teacher_id);

    /**
     * Удаляет расписание по идентификатору подгруппы.
     *
     * @param subgroup_id идентификатор подгруппы
     */
    void deleteBySubgroupId(int subgroup_id);

    void deleteBySubgroupNumber(String subgroup_number);

    /**
     * Находит расписание по номеру подгруппы и типу предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param subject_type тип предмета
     * @return список расписаний
     */
    List<Schedule> findBySubgroupNumberAndSubjectType(String subgroup_number, String subject_type);

    /**
     * Находит расписание по номеру подгруппы и имени преподавателя.
     *
     * @param subgroup_number номер подгруппы
     * @param teacher_name имя преподавателя
     * @return список расписаний
     */
    List<Schedule> findBySubgroupNumberAndTeacherName(String subgroup_number, String teacher_name);

    /**
     * Находит расписание по номеру подгруппы и названию предмета.
     *
     * @param subgroup_number номер подгруппы
     * @param subject_name название предмета
     * @return список расписаний
     */
    List<Schedule> findBySubgroupNumberAndSubjectName(String subgroup_number, String subject_name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM schedule", nativeQuery = true)
    void truncateSchedule();
}
