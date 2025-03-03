package org.ksu.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.service.impl.TeacherServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;

    @Mock
    private FacultyRepository facultyRepository;

    private Faculty testFaculty;

    @BeforeEach
    public void setUp() {
        testFaculty = Faculty.builder()
                .id(1)
                .facultyName("Math Faculty")
                .abbreviation("MF")
                .build();

        testTeacher = Teacher.builder()
                .id(1)
                .name("John Doe")
                .post("Docent")
                .faculty(testFaculty)
                .build();
    }

//    @Test
//    public void testInsert(Teacher teacher) {
//        when(teacherRepository.saveAndFlush(any(Teacher.class))).thenReturn(testTeacher);
//
//        Teacher result = teacherService.insert(teacher);
//
//        assertNotNull(result);
//        assertEquals(testTeacher.getId(), result.getId());
//        assertEquals(testTeacher.getName(), result.getName());
//        assertEquals(testTeacher.getPost(), result.getPost());
//        assertEquals(testTeacher.getFaculty(), result.getFaculty());
//
//        verify(teacherRepository, times(1)).saveAndFlush(teacher);
//    }
}
