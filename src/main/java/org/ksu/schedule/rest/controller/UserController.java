package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Контроллер для управления пользователями.
 *
 * @version 1.0
 * @автор Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    /**
     * Получить пользователя по email.
     *
     * @param email Email пользователя.
     * @return Ответ с пользователем.
     */
    @GetMapping("/user")
    public ResponseEntity<Optional<User>> getUser(@RequestParam String email) {
        return ResponseEntity.ok().body(userService.getByEmail(email));
    }

    /**
     * Обновить информацию студента.
     *
     * @param email     Email студента.
     * @param lastName  Фамилия студента.
     * @param firstName Имя студента.
     * @param middleName Отчество студента.
     * @return Ответ с обновленной информацией студента.
     */
    @PutMapping("/user/update/student/{email}")
    public ResponseEntity<Optional<User>> updateStudent(@PathVariable String email,
                                                        @RequestParam String lastName,
                                                        @RequestParam String firstName,
                                                        @RequestParam String middleName) {
        return ResponseEntity.ok().body(userService.updateStudent(email, lastName, firstName, middleName));
    }

    /**
     * Обновить информацию преподавателя.
     *
     * @param email     Email преподавателя.
     * @param lastName  Фамилия преподавателя.
     * @param firstName Имя преподавателя.
     * @param middleName Отчество преподавателя.
     * @param info      Информация о преподавателе.
     * @return Ответ с обновленной информацией преподавателя.
     */
    @PutMapping("/user/update/teacher/{email}")
    public ResponseEntity<Optional<User>> updateTeacher(@PathVariable String email,
                                                        @RequestParam String lastName,
                                                        @RequestParam String firstName,
                                                        @RequestParam String middleName,
                                                        @RequestParam String info) {
        return ResponseEntity.ok().body(userService.updateTeacher(email, lastName, firstName, middleName, info));
    }

    /**
     * Получить преподавателя по ID.
     *
     * @param id Идентификатор преподавателя.
     * @return Ответ с преподавателем.
     */
    @GetMapping("/user/teacher/{id}")
    public ResponseEntity<Optional<User>> getTeacher(@PathVariable int id) {
        return ResponseEntity.ok().body(userService.getByTeacherId(id));
    }
}
