package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями {@link Photo}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    /**
     * Находит фото по email пользователя.
     *
     * @param id email пользователя
     * @return Optional с фото, если найдено
     */
    Optional<Photo> findById(Long id);
}
