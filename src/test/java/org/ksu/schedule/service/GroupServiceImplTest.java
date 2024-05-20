package org.ksu.schedule.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.service.impl.GroupServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс GroupServiceImpl")
@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {

    public static final int EXISTING_NUMBER1 = 109;
    public static final String EXISTING_DIRECTION1 = "ИВТ";

    public static final String EXISTING_PROFILE1 = "ИИ";

    public static final int EXISTING_NUMBER2 = 313;
    public static final int EXISTING_NUMBER3 = 167;
    public static final String EXISTING_DIRECTION2 = "Педагог";
    public static final String EXISTING_DIRECTION3 = "МОАИС";
    public static final String EXISTING_PROFILE2 = "Информатика английский";
    public static final String EXISTING_PROFILE3 = "Нет";


    private GroupService groupService;

    @Mock
    private GroupRepository groupRepository;

    private List<Group> groups;

    @BeforeEach
    void init() {

        groups = new ArrayList<>();

        Group expectedGroup1 = Group.builder()
                .number(EXISTING_NUMBER1)
                .profile(EXISTING_PROFILE1)
                .direction(EXISTING_DIRECTION1)
                .build();

        Group expectedGroup2 = Group.builder()
                .number(EXISTING_NUMBER2)
                .profile(EXISTING_PROFILE2)
                .direction(EXISTING_DIRECTION2)
                .build();

        Group expectedGroup3 = Group.builder()
                .number(EXISTING_NUMBER3)
                .profile(EXISTING_PROFILE3)
                .direction(EXISTING_DIRECTION3)
                .build();


        groups.add(expectedGroup1);
        groups.add(expectedGroup2);
        groups.add(expectedGroup3);

        groupService = new GroupServiceImpl(groupRepository);
    }

    @DisplayName("должен получать все группы")
    @Test
    void shouldGetAllGroups() {

        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> expectedGroups = groupService.getAll();

        assertThat(expectedGroups).isEqualTo(groups);
    }

    @DisplayName("должен получать группу по номеру")
    @Test
    void shouldGetGroupByNumber() {
        Group expectedGroup = Group.builder()
                .number("413")
                .direction("МОАИС")
                .profile("1")
                .build();
        when(groupRepository.findByNumber("413")).thenReturn(expectedGroup);

        Group actualGroup = groupService.getByNumber("413");

        assertThat(expectedGroup).isEqualTo(actualGroup);
    }

    @DisplayName("должен получать группы по направлению")
    @Test
    void shouldGetGroupByDirection() {
        Group expectedGroups = Group.builder()
                .number("678")
                .direction("IVT")
                .profile("Прикладной ии")
                .build();

        when(groupRepository.findByDirection("IVT")).thenReturn(Collections.singletonList(expectedGroups));

        List<Group> actualGroups = groupService.getByDirection("IVT");

        assertThat(expectedGroups).isIn(actualGroups);
    }

    @DisplayName("должен получать группы по профилю")
    @Test
    void shouldGetGroupsByProfile() {
        Group expectedGroups = Group.builder()
                .number(234)
                .direction("Педагоги")
                .profile("AI")
                .build();
        when(groupRepository.findByProfile("AI")).thenReturn(Collections.singletonList(expectedGroups));

        List<Group> actualGroups = groupService.getByProfile("AI");

        assertThat(expectedGroups).isIn(actualGroups);
    }

}
