package org.ksu.schedule.service.impl;

import org.ksu.schedule.domain.Photo;
import org.ksu.schedule.domain.Role;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.PhotoRepository;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.repository.UserRepository;
import org.ksu.schedule.service.FileStorageService;
import org.ksu.schedule.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Реализация интерфейса {@link PhotoService}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Загружает фото для пользователя.
     *
     * @param email Email пользователя
     * @param file  Файл с фото
     */
    @Override
    public void uploadPhoto(String email, MultipartFile file) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Удаление старого фото, если оно существует
            if (user.getPhoto() != null) {
                String oldPhotoUrl = user.getPhoto().getUrl();
                try {
                    fileStorageService.deleteFile(oldPhotoUrl);
                } catch (Exception e) {
                    System.err.println("Не удалось удалить старый файл: " + e.getMessage());
                }
                photoRepository.delete(user.getPhoto());
            }

            // Сохранение нового фото
            String fileUrl = fileStorageService.storeFile(file);
            Photo photo = new Photo();
            photo.setUrl(fileUrl);

            // Проверка существования записи с таким user_email в таблице photo
            Optional<Photo> existingPhotoOptional = photoRepository.findByUserEmail(email);
            if (existingPhotoOptional.isPresent()) {
                Photo existingPhoto = existingPhotoOptional.get();
                existingPhoto.setUrl(fileUrl);
                photoRepository.save(existingPhoto);
            } else {
                photoRepository.save(photo);
                user.setPhoto(photo);
                userRepository.save(user);
            }
        }
    }

    /**
     * Сохраняет фото.
     *
     * @param file Файл с фото
     * @return URL сохраненного файла
     */
    @Override
    public String savePhoto(MultipartFile file) {
        return fileStorageService.storeFile(file);
    }


    /**
     * Получает фото по его ID.
     *
     * @param id ID фото
     * @return Фото
     */
    @Override
    public Photo getById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Photo photo = user.getPhoto();
            if (photo != null) {
                System.out.println("URL фото: " + photo.getUrl());
            }
            return photo;
        }
        return null;
    }
    
}
