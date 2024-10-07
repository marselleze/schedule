package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностями {@link Teacher}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    /**
     * Находит преподавателя по имени.
     *
     * @param nameTeacher имя преподавателя
     * @return преподаватель
     */
    Teacher findByName(String nameTeacher);

    /**
     * Находит преподавателя по имени и должности.
     *
     * @param nameTeacher имя преподавателя
     * @param post должность преподавателя
     * @return преподаватель
     */
    @Query("SELECT DISTINCT s FROM Teacher s WHERE s.name = :name AND s.post = :post")
    Teacher findByNameAndPost(@Param("name") String nameTeacher, @Param("post") String post);

    /**
     * Находит одного преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя
     * @return преподаватель
     */
    Teacher findOneById(int id);

    /**
     * Находит преподавателей по идентификатору.
     *
     * @param id идентификатор преподавателя
     * @return список преподавателей
     */
    @Query("SELECT t FROM Teacher t WHERE t.id = :id")
    List<Teacher> findByTeacherId(@Param("id") int id);

    /**
     * Находит преподавателей по должности.
     *
     * @param post должность преподавателя
     * @return список преподавателей
     */
    List<Teacher> findByPost(String post);

    /**
     * Удаляет преподавателя по имени.
     *
     * @param nameTeacher имя преподавателя
     */
    void deleteByName(String nameTeacher);

    /**
     * Удаляет преподавателей по должности.
     *
     * @param post должность преподавателя
     */
    void deleteByPost(String post);

    /**
     * Проверяет, существует ли преподаватель с заданным именем.
     *
     * @param name имя преподавателя
     * @return true, если преподаватель существует, иначе false
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Teacher t WHERE t.name = :name")
    boolean existsByName(@Param("name") String name);
}
