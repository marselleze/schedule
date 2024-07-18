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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
            photo.setUser(user);
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

            // Обновление фото для преподавателя, если пользователь - преподаватель
            if (user.getRole() == Role.TEACHER) {
                String name = user.getLastName() + " " + user.getFirstName().charAt(0) + "." + user.getMiddleName().charAt(0) + ".";
                Optional<Teacher> teacherOptional = Optional.ofNullable(teacherRepository.findByName(name));
                if (teacherOptional.isPresent()) {
                    Teacher teacher = teacherOptional.get();
                    teacher.setPhoto(photo);
                    teacherRepository.save(teacher);
                } else {
                    Teacher teacher = new Teacher();
                    teacher.setName(name);
                    teacher.setPhoto(photo);
                    teacherRepository.save(teacher);
                }
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
    public Photo getById(int id) {
        Optional<User> userOptional = userRepository.findByTeacherId(id);
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

    /**
     * Получает фото по Email пользователя.
     *
     * @param email Email пользователя
     * @return Фото
     */
    @Override
    public Photo getPhotoByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPhoto();
        }
        return null;
    }

    /**
     * Получает фото по ID преподавателя.
     *
     * @param id ID преподавателя
     * @return Фото
     */
    @Override
    public Photo getPhotoByTeacherId(int id) {
        List<Teacher> teachers = teacherRepository.findByTeacherId(id);
        if (teachers.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Преподаватель не найден");
        } else if (teachers.size() > 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Найдено несколько преподавателей с одинаковым ID");
        } else {
            Teacher teacher = teachers.get(0);
            return teacher.getPhoto();
        }
    }
}
