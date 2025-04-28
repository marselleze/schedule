package org.ksu.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.service.impl.FacultyServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceImplTest {

    @InjectMocks
    private FacultyServiceImpl facultyService;

    @Mock
    private FacultyRepository facultyRepository;

    private Faculty testFaculty;

    @BeforeEach
    public void setUp() {
        testFaculty = Faculty.builder()
                .id(1)
                .facultyName("Math faculty")
                .abbreviation("MF")
                .build();

    }

    @Test
    public void testGetById(){
        when(facultyRepository.findById(1)).thenReturn(testFaculty);

        Faculty result = facultyService.getFacultyById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Math faculty", result.getFacultyName());
        assertEquals("MF", result.getAbbreviation());

        verify(facultyRepository, times(1)).findById(1);
    }

    @Test
    public void testAdd(){
        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);

        Faculty result = facultyService.addFaculty(testFaculty);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Math faculty", result.getFacultyName());
        assertEquals("MF", result.getAbbreviation());

        verify(facultyRepository, times(1)).save(any(Faculty.class));
    }

//    @Test
//    public void testUpdate(){
//        when(facultyRepository.save(any(Faculty.class))).thenReturn(testFaculty);
//
//        Faculty result = facultyService.updateFaculty(1, "Programming faculty", "PF");
//
//        assertNotNull(result);
//        assertEquals(1, result.getId());
//        assertEquals("Programming faculty", result.getFacultyName());
//        assertEquals("PF", result.getAbbreviation());
//
//        verify(facultyRepository, times(1)).save(any(Faculty.class));
//    }

    @Test
    public void testGetAll(){
        List<Faculty> faculties = Collections.singletonList(testFaculty);
        when(facultyRepository.findAll()).thenReturn(faculties);

        List<Faculty> result = facultyService.getAllFaculty();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testFaculty, result.get(0));
        assertEquals(1, faculties.get(0).getId());
        assertEquals("Math faculty", faculties.get(0).getFacultyName());
        assertEquals("MF", faculties.get(0).getAbbreviation());

        verify(facultyRepository, times(1)).findAll();
    }

    @Test
    public void testGetByFacultyName() {
        when(facultyRepository.findByFacultyName("Math faculty")).thenReturn(testFaculty);

        Faculty result = facultyService.getFacultyByFacultyName("Math faculty");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Math faculty", result.getFacultyName());
        assertEquals("MF", result.getAbbreviation());

        verify(facultyRepository, times(1)).findByFacultyName("Math faculty");
    }

    @Test
    public void testDelete(){
        facultyService.deleteFaculty(1);

        verify(facultyRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetByAbbreviation(){
        when(facultyRepository.findByAbbreviation("MF")).thenReturn(testFaculty);

        Faculty result = facultyService.getFacultyByAbbreviation("MF");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Math faculty", result.getFacultyName());
        assertEquals("MF", result.getAbbreviation());

        verify(facultyRepository, times(1)).findByAbbreviation("MF");
    }
}
