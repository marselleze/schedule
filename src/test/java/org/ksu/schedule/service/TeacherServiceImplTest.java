package org.ksu.schedule.service;


import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.service.impl.TeacherServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс TeacherServiceImpl")
@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    public static final int EXISTING_ID1 = 1;
    public static final String EXISTING_NAME1 = "Иван Иванов";

    public static final String EXISTING_POST1 = "Декан";

    public static final int EXISTING_ID2 = 2;
    public static final int EXISTING_ID3 = 3;
    public static final String EXISTING_NAME2 = "Овсянников Алекснадр Владимирович";
    public static final String EXISTING_NAME3 = "Толостова Галина Семеновна";
    public static final String EXISTING_POST2 = "Аспирант";
    public static final String EXISTING_POST3 = "Зав. кафедрой";


    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;


    private List<Teacher> teachers;


    @BeforeEach
    void init() {
        teachers = new ArrayList<>();

        Teacher expectedTeacher1 = Teacher.builder()
                .id(EXISTING_ID1)
                .name(EXISTING_NAME1)
                .post(EXISTING_POST1)
                .build();
        Teacher expectedAuthor2 = Teacher.builder()
                .id(EXISTING_ID2)
                .name(EXISTING_NAME2)
                .post(EXISTING_POST2)
                .build();
        Teacher expectedAuthor3 = Teacher.builder()
                .id(EXISTING_ID3)
                .name(EXISTING_NAME3)
                .post(EXISTING_POST3)
                .build();

        teachers.add(expectedTeacher1);
        teachers.add(expectedAuthor2);
        teachers.add(expectedAuthor3);

        teacherService = new TeacherServiceImpl(teacherRepository);
    }


    @DisplayName("должен получать всех преподов")
    @Test
    void shouldGetAllTeacher() {

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> expectedTeachers = teacherService.getAll();

        assertThat(expectedTeachers).isEqualTo(teachers);
    }

    @DisplayName("должен получать препода по имени")
    @Test
    void shouldGetTeacherByName() {
        Teacher expectedTeacher = Teacher.builder()
                .id(4)
                .post("Who")
                .name("Ivan")
                .build();

        when(teacherRepository.findByName("Ivan")).thenReturn(expectedTeacher);

        Teacher actualTeacher = teacherService.getByName("Ivan");

        assertThat(expectedTeacher).isEqualTo(actualTeacher);
    }

    @DisplayName("должен получать преподов по должности")
    @Test
    void shouldGetTeacherByPost() {
        Teacher expectedTeacher = Teacher.builder()
                .id(3)
                .post("Старший преподаватель")
                .name("Владимир")
                .build();


        when(teacherRepository.findByPost("Старший преподаватель")).thenReturn(Collections.singletonList(expectedTeacher));

        List<Teacher> actualTeachers = teacherService.getByPost("Старший преподаватель");

        assertThat(expectedTeacher).isIn(actualTeachers);
    }


}
