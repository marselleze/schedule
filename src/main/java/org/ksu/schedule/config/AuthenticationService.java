package org.ksu.schedule.config;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.domain.User;
import org.ksu.schedule.repository.FacultyRepository;
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
    private final FacultyRepository facultyRepository;

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

        Faculty faculty = request.getFaculty();
        if (faculty != null) {
            faculty = facultyRepository.findById(request.getFaculty().getId()).orElseThrow(() -> new IllegalArgumentException("Invalid faculty ID"));
        }



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
                .faculty(faculty)
                .build();

        // Сохраняем пользователя в базу данных и получаем сохраненного пользователя с ID
        User savedUser = userRepository.save(user);

        // Генерация JWT токена
        String jwtToken = jwtService.generateToken(savedUser);

        // Логирование сгенерированного токена
        logger.info("Generated token: {}", jwtToken);

        // Возвращаем ответ с токеном
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(savedUser.getId())
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
        return new AuthenticationResponse(jwtToken, user.getId());
    }
}
