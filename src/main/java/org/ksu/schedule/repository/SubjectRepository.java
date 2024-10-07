package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Subject}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {

    /**
     * Находит предметы по имени.
     *
     * @param nameSubject имя предмета
     * @return список предметов
     */
    List<Subject> findByName(String nameSubject);

    /**
     * Находит один предмет по имени.
     *
     * @param nameSubject имя предмета
     * @return предмет
     */
    Subject findOneByName(String nameSubject);

    /**
     * Находит один предмет по идентификатору.
     *
     * @param id идентификатор предмета
     * @return предмет
     */
    Subject findOneById(int id);

    /**
     * Находит предмет по имени и типу.
     *
     * @param nameSubject имя предмета
     * @param type тип предмета
     * @return предмет
     */
    @Query("SELECT DISTINCT s FROM Subject s WHERE s.name = :name AND s.type = :type")
    Subject findByNameAndType(@Param("name") String nameSubject, @Param("type") String type);

    /**
     * Находит предметы по типу.
     *
     * @param type тип предмета
     * @return список предметов
     */
    List<Subject> findByType(String type);

    /**
     * Удаляет предметы по имени.
     *
     * @param nameSubject имя предмета
     */
    void deleteByName(String nameSubject);

    /**
     * Удаляет предметы по типу.
     *
     * @param type тип предмета
     */
    void deleteByType(String type);

    /**
     * Находит идентификатор предмета по имени и типу.
     *
     * @param name имя предмета
     * @param type тип предмета
     * @return идентификатор предмета
     */
    Long findIdByNameAndType(String name, String type);
}
