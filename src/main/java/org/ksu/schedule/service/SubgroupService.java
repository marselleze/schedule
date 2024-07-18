package org.ksu.schedule.service;

import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;

/**
 * Сервис для управления подгруппами.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface SubgroupService {

    /**
     * Вставить новую подгруппу.
     *
     * @param id идентификатор подгруппы
     * @param number номер подгруппы
     * @param group_number номер группы
     * @return созданная подгруппа
     */
    Subgroup insert(int id, String number, String group_number);

    /**
     * Обновить подгруппу.
     *
     * @param id идентификатор подгруппы
     * @param number номер подгруппы
     * @param group_number номер группы
     * @return обновленная подгруппа
     */
    Subgroup update(int id, String number, String group_number);

    /**
     * Получить подгруппу по номеру.
     *
     * @param number номер подгруппы
     * @return найденная подгруппа
     */
    Subgroup getByNumber(String number);

    /**
     * Получить подгруппы по идентификатору группы.
     *
     * @param group_id идентификатор группы
     * @return список подгрупп
     */
    List<Subgroup> getByGroupId(int group_id);

    /**
     * Удалить подгруппу по идентификатору.
     *
     * @param id идентификатор подгруппы
     */
    void deleteById(int id);

    /**
     * Удалить подгруппы по идентификатору группы.
     *
     * @param group_id идентификатор группы
     */
    void deleteByGroupId(int group_id);

    /**
     * Получить все подгруппы.
     *
     * @return список всех подгрупп
     */
    List<Subgroup> getAll();

    /**
     * Получить подгруппы по номеру группы.
     *
     * @param groupNumber номер группы
     * @return список подгрупп
     */
    List<Subgroup> getByGroupNumber(String groupNumber);
}
