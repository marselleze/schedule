package org.ksu.schedule.service;

import org.ksu.schedule.domain.Teacher;

import java.util.List;

/**
 * Сервис для управления преподавателями.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface TeacherService {

    /**
     * Вставить нового преподавателя.
     *
     * @param teacher объект преподавателя
     * @return созданный преподаватель
     */
    Teacher insert(Teacher teacher);

    /**
     * Обновить информацию о преподавателе.
     *
     * @param id идентификатор преподавателя
     * @param teacherName имя преподавателя
     * @param post должность преподавателя
     * @return обновленный преподаватель
     */
    Teacher update(int id, String teacherName, String post);

    /**
     * Получить преподавателя по имени.
     *
     * @param teacherName имя преподавателя
     * @return объект преподавателя
     */
    Teacher getByName(String teacherName);

    /**
     * Получить преподавателей по должности.
     *
     * @param post должность преподавателя
     * @return список преподавателей
     */
    List<Teacher> getByPost(String post);

    /**
     * Удалить преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя
     */
    void deleteById(int id);

    /**
     * Удалить преподавателя по имени.
     *
     * @param teacherName имя преподавателя
     */
    void deleteByName(String teacherName);

    /**
     * Удалить преподавателей по должности.
     *
     * @param post должность преподавателя
     */
    void deleteByPost(String post);

    /**
     * Получить всех преподавателей.
     *
     * @return список всех преподавателей
     */
    List<Teacher> getAll();

    /**
     * Получить преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя
     * @return объект преподавателя
     */
    Teacher getById(int id);
}
