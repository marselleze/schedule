package org.ksu.schedule.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.repository.SubjectRepository;
import org.ksu.schedule.service.impl.SubjectServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("Класс SubjectServiceImpl")
public class SubjectServiceImplTest {

    public static int EXISTING_ID1 = 1;
    public static int EXITING_ID2 = 2;
    public static int EXISTING_ID3 = 3;

    public static String EXISTING_NAME1 = "Test";
    public static String EXITING_NAME2 = "Test2";
    public static String EXISTING_NAME3 = "Test3";

    public static String EXISTING_TYPE1 = "TestType";
    public static String EXISTING_TYPE2 = "TestType2";
    public static String EXISTING_TYPE3 = "TestType3";


    private SubjectService subjectService;

    @Mock
    private SubjectRepository subjectRepository;

    private List<Subject> subjects;

    @BeforeEach
    void init() {

        subjects = new ArrayList<>();

        Subject expectedSubject1 = Subject.builder()
                .id(EXISTING_ID1)
                .name(EXISTING_NAME1)
                .type(EXISTING_TYPE1)
                .build();

        Subject expectedSubject2 = Subject.builder()
                .id(EXITING_ID2)
                .name(EXITING_NAME2)
                .type(EXISTING_TYPE2)
                .build();

        Subject expectedSubject3 = Subject.builder()
                .id(EXISTING_ID3)
                .name(EXISTING_NAME3)
                .type(EXISTING_TYPE3)
                .build();

        subjects.add(expectedSubject1);
        subjects.add(expectedSubject2);
        subjects.add(expectedSubject3);

        subjectService = new SubjectServiceImpl(subjectRepository);
    }

    @DisplayName("Должен возвращать список всех предметов")
    @Test
    void getAllSubjects() {
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<Subject> actualSubjects = subjectService.getAll();

        assertThat(actualSubjects).isEqualTo(subjects);
    }

//    @DisplayName("должен выводить предмет по его назвнию")
//    @Test
//    void getSubjectByName() {
//        Subject expectedSubject = Subject.builder()
//                .id(1)
//                .name("Test")
//                .type("TestType")
//                .build();
//
//        when(subjectRepository.findByName("Test")).thenReturn(Collections.singletonList(expectedSubject));
//
//        List<Subject> actualSubjects = subjectService.getByName("Test");
//
//        assertThat(actualSubjects).isEqualTo(Collections.singletonList(expectedSubject));
//    }

    @DisplayName("должен выводить список предметов по их типу")
    @Test
    void getSubjectByType() {
        Subject expectedSubject = Subject.builder()
                .id(2)
                .name("Test")
                .type("TestType")
                .build();

        when(subjectRepository.findByType("TestType")).thenReturn(Collections.singletonList(expectedSubject));

        List<Subject> actualSubjects = subjectService.getByType("TestType");

        assertThat(actualSubjects).isEqualTo(Collections.singletonList(expectedSubject));
    }



}
