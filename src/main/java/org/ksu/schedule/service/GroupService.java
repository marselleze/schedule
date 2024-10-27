package org.ksu.schedule.service;

import org.ksu.schedule.domain.Group;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Сервис для управления группами.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface GroupService {

    /**
     * Добавляет новую группу.
     *
     * @param group объект группы для добавления
     * @return добавленный объект группы
     */
    Group addGroup(Group group);

    /**
     * Удаляет группу по идентификатору.
     *
     * @param id идентификатор группы
     */
    void deleteById(int id);

    /**
     * Получает группу по номеру.
     *
     * @param number номер группы
     * @return объект группы
     */
    Group getByNumber(String number);

    /**
     * Получает группы по направлению.
     *
     * @param direction направление группы
     * @return список групп с заданным направлением
     */
    List<Group> getByDirection(String direction);

    /**
     * Получает группы по профилю.
     *
     * @param profile профиль группы
     * @return список групп с заданным профилем
     */
    List<Group> getByProfile(String profile);

    /**
     * Обновляет данные группы.
     *
     * @param id идентификатор группы
     * @param number номер группы
     * @param direction направление группы
     * @param profile профиль группы
     * @return обновленный объект группы
     */
    Group updateGroup(int id, String number, String direction, String profile);

    /**
     * Получает все группы.
     *
     * @return список всех групп
     */
    List<Group> getAll();

    List<Group> getByFacultyName(String facultyName);

    List<Group> getByFacultyAbb(String facultyAbb);
}
