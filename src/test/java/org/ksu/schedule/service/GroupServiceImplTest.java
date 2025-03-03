package org.ksu.schedule.service;


import org.ksu.schedule.domain.Faculty;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.service.impl.GroupServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group testGroup;

    private Faculty testFaculty;

    @BeforeEach
    public void setUp() {

        testFaculty = Faculty.builder()
                .id(1)
                .facultyName("Math")
                .abbreviation("MT")
                .build();

        testGroup = Group.builder()
                .id(1)
                .number("1234")
                .direction("Computer Science")
                .profile("Software Engineering")
                .faculty(testFaculty)
                .build();

    }

    @Test
    public void testAddGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(testGroup);

        Group result = groupService.addGroup(testGroup);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("1234", result.getNumber());
        assertEquals("Computer Science", result.getDirection());
        assertEquals("Software Engineering", result.getProfile());

        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    public void testDeleteById() {
        groupService.deleteById(1);

        verify(groupRepository, times(1)).deleteById(1);
    }

    @Test
    public void testGetByNumber() {
        when(groupRepository.findByNumber("1234")).thenReturn(testGroup);

        Group result = groupService.getByNumber("1234");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("1234", result.getNumber());
        assertEquals("Computer Science", result.getDirection());
        assertEquals("Software Engineering", result.getProfile());

        verify(groupRepository, times(1)).findByNumber("1234");
    }

    @Test
    public void testGetByDirection() {
        List<Group> groups = Collections.singletonList(testGroup);
        when(groupRepository.findByDirection("Computer Science")).thenReturn(groups);

        List<Group> result = groupService.getByDirection("Computer Science");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("1234", result.get(0).getNumber());
        assertEquals("Computer Science", result.get(0).getDirection());
        assertEquals("Software Engineering", result.get(0).getProfile());

        verify(groupRepository, times(1)).findByDirection("Computer Science");
    }

    @Test
    public void testGetByProfile() {
        List<Group> groups = Collections.singletonList(testGroup);
        when(groupRepository.findByProfile("Software Engineering")).thenReturn(groups);

        List<Group> result = groupService.getByProfile("Software Engineering");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("1234", result.get(0).getNumber());
        assertEquals("Computer Science", result.get(0).getDirection());
        assertEquals("Software Engineering", result.get(0).getProfile());

        verify(groupRepository, times(1)).findByProfile("Software Engineering");
    }

//    @Test
//    public void testUpdateGroup() {
//        when(facultyRepository.findById(1)).thenReturn(testFaculty);
//        when(groupRepository.saveAndFlush(any(Group.class))).thenReturn(testGroup);
//
//        Group result = groupService.updateGroup(1, "123", "Computer", "Software", 1);
//
//        assertNotNull(result);
//        assertEquals(1, result.getId());
//        assertEquals("123", result.getNumber());
//        assertEquals("Computer", result.getDirection());
//        assertEquals("Software", result.getProfile());
//
//        verify(facultyRepository, times(1)).findById(1);
//        verify(groupRepository, times(1)).saveAndFlush(any(Group.class));
//    }

    @Test
    public void testGetAll() {
        List<Group> groups = Collections.singletonList(testGroup);
        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> result = groupService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("1234", result.get(0).getNumber());
        assertEquals("Computer Science", result.get(0).getDirection());
        assertEquals("Software Engineering", result.get(0).getProfile());

        verify(groupRepository, times(1)).findAll();
    }

    @Test
    public void testGetByFacultyName() {
        List<Group> groups = Collections.singletonList(testGroup);
        when(groupRepository.findByFaculty_FacultyName("Math")).thenReturn(groups);

        List<Group> result = groupService.getByFacultyName("Math");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("1234", result.get(0).getNumber());
        assertEquals("Computer Science", result.get(0).getDirection());
        assertEquals("Software Engineering", result.get(0).getProfile());

        verify(groupRepository, times(1)).findByFaculty_FacultyName("Math");
    }

    @Test
    public void testGetByFacultyAbb() {
        List<Group> groups = Collections.singletonList(testGroup);
        when(groupRepository.findByFaculty_Abbreviation("MT")).thenReturn(groups);

        List<Group> result = groupService.getByFacultyAbb("MT");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("1234", result.get(0).getNumber());
        assertEquals("Computer Science", result.get(0).getDirection());
        assertEquals("Software Engineering", result.get(0).getProfile());

        verify(groupRepository, times(1)).findByFaculty_Abbreviation("MT");
    }
}

