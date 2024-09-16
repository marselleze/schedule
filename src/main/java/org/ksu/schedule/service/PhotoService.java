package org.ksu.schedule.service;

import org.ksu.schedule.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для управления фотографиями.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface PhotoService {

    /**
     * Загружает фотографию по указанному email.
     *
     * @param email email пользователя
     * @param file файл фотографии
     */
    void uploadPhoto(String email, MultipartFile file);

    /**
     * Сохраняет фотографию.
     *
     * @param file файл фотографии
     * @return путь к сохраненной фотографии
     */
    String savePhoto(MultipartFile file);

    /**
     * Получает фотографию по идентификатору.
     *
     * @param id идентификатор фотографии
     * @return объект фотографии
     */

    Photo getById(Long id);

}
