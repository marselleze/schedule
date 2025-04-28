package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.repository.UserRepository;
import org.ksu.schedule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

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
    private final FacultyRepository facultyRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

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


    @Override
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        logger.info("Password for user {} was updated. Encoded password: {}" + user.getEmail() + encodedPassword);
    }

    @Override
    public Optional<User> updateStudentGroupAndFaculty(String email, String groupName, String subgroupName, String facultyName) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setGroup_number(groupName);
            user.get().setSubgroup_number(subgroupName);
            user.get().setFaculty(facultyRepository.findByFacultyName(facultyName));
            userRepository.save(user.get());
        }
        return user;
    }

    @Override
    public User findUserByFullName(String fullName) {


        // Убираем лишние пробелы и нормализуем ввод
        fullName = fullName.trim();


        // Разделяем ФИО на части
        String[] nameParts = fullName.split("\\s+");


        // Проверяем, что формат соответствует "Фамилия И.О." или "Фамилия И.О."
        if (nameParts.length < 2 || nameParts.length > 3) {
            throw new IllegalArgumentException("Полное имя должно содержать две или три части: Фамилия И.О. или Фамилия И.О.");
        }

        String lastName = nameParts[0];
        String firstInitial = "";
        String middleInitial = "";

        if (nameParts.length == 2) {
            // Если имя и отчество объединены
            if (nameParts[1].length() != 4 || !nameParts[1].endsWith(".")) {
                throw new IllegalArgumentException("Имя и отчество должны быть в формате инициалов: И.О.");
            }
            firstInitial = nameParts[1].substring(0, 1).toUpperCase();
            middleInitial = nameParts[1].substring(2, 3).toUpperCase();
        } else {
            // Если имя и отчество разделены
            if (nameParts[1].length() != 2 || nameParts[2].length() != 2) {
                throw new IllegalArgumentException("Имя и отчество должны быть в формате инициалов: И.О.");
            }
            if (!nameParts[1].endsWith(".") || !nameParts[2].endsWith(".")) {
                throw new IllegalArgumentException("Инициал имени и отчества должны заканчиваться точкой.");
            }
            firstInitial = nameParts[1].substring(0, 1).toUpperCase();
            middleInitial = nameParts[2].substring(0, 1).toUpperCase();
        }

        // Выполняем поиск в базе данных
        String finalFullName = fullName;
        return userRepository.findByLastNameAndInitials(lastName, firstInitial, middleInitial)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Пользователь с ФИО " + finalFullName + " не найден.");
                });
    }






}
