package org.ksu.schedule.service;

import org.ksu.schedule.domain.Subject;

import java.util.List;

/**
 * Сервис для управления предметами.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface SubjectService {

    /**
     * Вставить новый предмет.
     *
     * @param subject объект предмета
     * @return созданный предмет
     */
    Subject insert(Subject subject);

    /**
     * Обновить предмет.
     *
     * @param id идентификатор предмета
     * @param nameSubject название предмета
     * @param type тип предмета
     * @return обновленный предмет
     */
    Subject update(int id, String nameSubject, String type);

    /**
     * Получить предметы по названию.
     *
     * @param nameSubject название предмета
     * @return список предметов
     */
    List<Subject> getByName(String nameSubject);

    /**
     * Получить предметы по типу.
     *
     * @param type тип предмета
     * @return список предметов
     */
    List<Subject> getByType(String type);

    /**
     * Удалить предмет по названию.
     *
     * @param nameSubject название предмета
     */
    void deleteByName(String nameSubject);

    /**
     * Удалить предметы по типу.
     *
     * @param type тип предмета
     */
    void deleteByType(String type);

    /**
     * Удалить предмет по идентификатору.
     *
     * @param id идентификатор предмета
     */
    void deleteById(int id);

    /**
     * Получить все предметы.
     *
     * @return список всех предметов
     */
    List<Subject> getAll();
}
