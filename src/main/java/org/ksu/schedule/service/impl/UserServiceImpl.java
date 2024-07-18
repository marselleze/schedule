package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.UserRepository;
import org.ksu.schedule.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Реализация интерфейса {@link UserService}.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Получает пользователя по email.
     *
     * @param email электронная почта пользователя
     * @return найденный пользователь
     */
    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Обновляет информацию о студенте.
     *
     * @param email электронная почта студента
     * @param lastName фамилия студента
     * @param firstName имя студента
     * @param middleName отчество студента
     * @return обновленный студент
     */
    @Override
    public Optional<User> updateStudent(String email, String lastName, String firstName, String middleName) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setLastName(lastName);
            user.get().setFirstName(firstName);
            user.get().setMiddleName(middleName);
            userRepository.save(user.get());
        }
        return user;
    }

    /**
     * Обновляет информацию о преподавателе.
     *
     * @param email электронная почта преподавателя
     * @param lastName фамилия преподавателя
     * @param firstName имя преподавателя
     * @param middleName отчество преподавателя
     * @param info информация о преподавателе
     * @return обновленный преподаватель
     */
    @Override
    public Optional<User> updateTeacher(String email, String lastName, String firstName, String middleName, String info) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setLastName(lastName);
            user.get().setFirstName(firstName);
            user.get().setMiddleName(middleName);
            user.get().setInfo(info);
            userRepository.save(user.get());
        }
        return user;
    }

    /**
     * Получает пользователя по идентификатору преподавателя.
     *
     * @param teacherId идентификатор преподавателя
     * @return найденный пользователь
     */
    @Override
    public Optional<User> getByTeacherId(int teacherId) {
        return userRepository.findByTeacherId(teacherId);
    }
}
