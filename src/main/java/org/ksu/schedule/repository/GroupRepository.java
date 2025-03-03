package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Group}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface GroupRepository extends JpaRepository<Group, Integer> {
    /**
     * Находит группы по направлению.
     *
     * @param direction направление группы
     * @return список групп, соответствующих направлению
     */
    List<Group> findByDirection(String direction);

    /**
     * Находит группы по профилю.
     *
     * @param profile профиль группы
     * @return список групп, соответствующих профилю
     */
    List<Group> findByProfile(String profile);

    /**
     * Находит идентификатор группы по номеру.
     *
     * @param number номер группы
     * @return идентификатор группы
     */
    Integer findGroupIdByNumber(String number);

    /**
     * Находит группу по номеру.
     *
     * @param number номер группы
     * @return группа, соответствующая номеру
     */
    Group findByNumber(String number);


    /**
     * Удаляет группу по номеру.
     *
     * @param number номер группы
     */
    void deleteByNumber(String number);

    List<Group> findByFaculty_FacultyName(String facultyName);

    List<Group> findByFaculty_Abbreviation(String abbreviation);

    Group findByNumberAndFacultyId(String number, Integer facultyId);
}
