package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Subgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Subgroup}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface SubgroupRepository extends JpaRepository<Subgroup, Integer> {

    /**
     * Находит подгруппы по идентификатору группы.
     *
     * @param group_id идентификатор группы
     * @return список подгрупп
     */
    List<Subgroup> findByGroupId(int group_id);

    /**
     * Удаляет подгруппы по идентификатору группы.
     *
     * @param group_id идентификатор группы
     */
    void deleteByGroupId(int group_id);

    /**
     * Находит подгруппу по номеру.
     *
     * @param number номер подгруппы
     * @return подгруппа
     */
    Subgroup findByNumber(String number);

    /**
     * Находит подгруппы по номеру группы.
     *
     * @param groupNumber номер группы
     * @return список подгрупп
     */
    List<Subgroup> findByGroupNumber(String groupNumber);

    void deleteByNumber(String numSubGroup);
}
