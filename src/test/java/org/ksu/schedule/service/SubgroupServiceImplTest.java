package org.ksu.schedule.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.repository.SubgroupRepository;
import org.ksu.schedule.service.impl.GroupServiceImpl;
import org.ksu.schedule.service.impl.SubgroupServiceImpl;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс SubgroupServiceImpl")
@ExtendWith(MockitoExtension.class)
class SubgroupServiceImplTest {

    public static final int EXISTING_ID1 = 1;
    public static final int EXISTING_ID2 = 2;
    public static final int EXISTING_ID3 = 3;
    public static final int EXISTING_NUMBER1 = 1;
    public static final int EXISTING_NUMBER2 = 2;
    public static final int EXISTING_NUMBER3 = 3;
    public static final int EXISTING_GROUPID1 = 1;
    public static final int EXISTING_GROUPID2 = 2;
    public static final int EXISTING_GROUPID3 = 3;

    private SubgroupService subgroupService;

    @Mock
    private SubgroupRepository subgroupRepository;

    private List<Subgroup> subgroups;

    @BeforeEach
    void init() {

        subgroups = new ArrayList<>();

        Subgroup expectedSubgroup1 = Subgroup.builder()
                .id(EXISTING_ID1)
                .number(EXISTING_NUMBER1)
                .build();

        Subgroup expectedSubgroup2 = Subgroup.builder()
                .id(EXISTING_ID2)
                .number(EXISTING_NUMBER2)
                .build();

        Subgroup expectedSubgroup3 = Subgroup.builder()
                .id(EXISTING_ID3)
                .number(EXISTING_NUMBER3)
                .build();

        subgroups.add(expectedSubgroup1);
        subgroups.add(expectedSubgroup2);
        subgroups.add(expectedSubgroup3);

        subgroupService = new SubgroupServiceImpl(subgroupRepository);
    }

    @DisplayName("Должен выводить все подгруппы")
    @Test
    void shouldGetAllSubgroups() {
        when(subgroupRepository.findAll()).thenReturn(subgroups);

        List<Subgroup> expectedSubgroups = subgroupService.getAll();

        assertThat(expectedSubgroups).isEqualTo(subgroups);
    }

    @DisplayName("должен выводить подгруппы по group_id")
    @Test
    void shouldGetSubgroupsByGroupId() {

        when(subgroupRepository.findByGroupId(EXISTING_GROUPID1)).thenReturn(subgroups);

        List<Subgroup> expectedSubgroups = subgroupService.getByGroupId(EXISTING_GROUPID1);

        assertThat(expectedSubgroups).isEqualTo(subgroups);
    }



}
