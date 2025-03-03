package org.ksu.schedule.service;

import org.ksu.schedule.domain.User;

import java.util.Optional;

/**
 * Сервис для управления пользователями.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface UserService {

    /**
     * Получить пользователя по электронной почте.
     *
     * @param email электронная почта пользователя
     * @return объект Optional, содержащий пользователя
     */
    Optional<User> getByEmail(String email);

    /**
     * Обновить данные студента.
     *
     * @param email электронная почта пользователя
     * @param lastName фамилия пользователя
     * @param firstName имя пользователя
     * @param middleName отчество пользователя
     * @return объект Optional, содержащий обновленного пользователя
     */
    Optional<User> updateStudent(String email, String lastName, String firstName, String middleName);

    /**
     * Обновить данные преподавателя.
     *
     * @param email электронная почта пользователя
     * @param lastName фамилия пользователя
     * @param firstName имя пользователя
     * @param middleName отчество пользователя
     * @param info дополнительная информация о пользователе
     * @return объект Optional, содержащий обновленного пользователя
     */
    Optional<User> updateTeacher(String email, String lastName, String firstName, String middleName, String info);


    void updatePassword(User user, String newPassword);

    Optional<User> updateStudentGroupAndFaculty(String email, String groupName, String subgroupName, String facultyName);

    User findUserByFullName(String fullName);
}
