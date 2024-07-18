package org.ksu.schedule.config;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.repository.UserRepository;
import org.ksu.schedule.rest.controller.AuthenticationRequest;
import org.ksu.schedule.rest.controller.AuthenticationResponse;
import org.ksu.schedule.rest.controller.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для обработки аутентификации и регистрации пользователей.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    /**
     * Регистрация нового пользователя.
     *
     * @param request запрос на регистрацию {@link RegisterRequest}
     * @return ответ с JWT токеном {@link AuthenticationResponse}
     * @throws IllegalArgumentException если имя, фамилия или отчество отсутствуют
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Проверка входных данных
        if (request.getFirstName() == null || request.getLastName() == null || request.getMiddleName() == null) {
            throw new IllegalArgumentException("Имя, фамилия и отчество обязательны для заполнения");
        }

        // Поиск преподавателя по фамилии и инициалам
        String lastName = request.getLastName();
        String firstNameInitial = request.getFirstName().charAt(0) + ".";
        String middleNameInitial = request.getMiddleName().charAt(0) + ".";

        // Создание пользователя
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .group_number(request.getGroup_number())
                .subgroup_number(request.getSubgroup_number())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // Сохраняем пользователя в базу данных и получаем сохраненного пользователя с ID
        User savedUser = userRepository.save(user);

        // Переменная для хранения идентификатора преподавателя
        Integer teacher_Id = -1;

        if ("TEACHER".equals(request.getRole().name())) {
            // Поиск преподавателя по фамилии и инициалам
            Optional<Teacher> teacherOpt = Optional.ofNullable(teacherRepository.findByName(lastName + " " + firstNameInitial + middleNameInitial));

            if (teacherOpt.isPresent()) {
                Teacher teacher = teacherOpt.get();
                teacher.setUserId(savedUser.getId()); // Устанавливаем ID пользователя в сущности Teacher
                teacherRepository.save(teacher); // Сохраняем изменения в базе данных
                teacher_Id = teacher.getId();
            } else {
                throw new RuntimeException("Преподаватель с указанными фамилией и инициалами не найден.");
            }
        }

        // Обновляем пользователя с идентификатором преподавателя, если он найден
        savedUser.setTeacherId(teacher_Id);
        userRepository.save(savedUser);

        // Генерация JWT токена
        String jwtToken = jwtService.generateToken(user);

        // Логирование сгенерированного токена
        logger.info("Generated token: {}", jwtToken);

        // Возвращаем ответ с токеном
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Аутентификация пользователя.
     *
     * @param request запрос на аутентификацию {@link AuthenticationRequest}
     * @return ответ с JWT токеном {@link AuthenticationResponse}
     * @throws IllegalArgumentException если пользователь не найден
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        logger.info("User found: {}", user);
        var jwtToken = jwtService.generateToken(user);
        logger.info("Generated token: {}", jwtToken);
        return new AuthenticationResponse(jwtToken);
    }
}
