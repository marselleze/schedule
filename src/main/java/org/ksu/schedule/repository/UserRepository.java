package org.ksu.schedule.repository;

import org.ksu.schedule.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями {@link User}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по электронной почте.
     *
     * @param email электронная почта пользователя
     * @return Optional с пользователем
     */
    Optional<User> findByEmail(String email);

    /**
     * Находит пользователя по идентификатору преподавателя.
     *
     * @param teacherId идентификатор преподавателя
     * @return Optional с пользователем
     */
    @Query("select u from User u where u.teacherId = :teacherId")
    Optional<User> findByTeacherId(@Param("teacherId") int teacherId);
}
