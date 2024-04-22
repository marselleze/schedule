package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.repository.SubgroupRepository;
import org.ksu.schedule.service.SubgroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubgroupServiceImpl implements SubgroupService {

    private final SubgroupRepository subgroupRepository;

    private final GroupRepository groupRepository;

    @Override
    public Subgroup insert(int id, String number, int group_id) {

        Group groupId = groupRepository.findById(group_id).orElse(null);

        if (groupId == null){
            return null; //TODO: throw exception
        }

        Subgroup subgroup = Subgroup.builder()
                .id(id)
                .number(number)
                .group(groupId)
                .build();
        return subgroupRepository.saveAndFlush(subgroup);
    }

    @Override
    public Subgroup update(int id, String number, int group_id) {
        Group groupId = groupRepository.findById(group_id).orElseThrow();

        return subgroupRepository.saveAndFlush(
                Subgroup.builder()
                        .id(id)
                        .number(number)
                        .group(groupId)
                        .build()
        );
    }


    @Override
    public Subgroup getByNumber(String number) {
        return subgroupRepository.findByNumber(number);
    }

    @Override
    public List<Subgroup> getByGroupId(int group_id) {
        return subgroupRepository.findByGroupId(group_id);
    }

    @Override
    public void deleteById(int id) {
        subgroupRepository.deleteById(id);
    }

    @Override
    public void deleteByGroupId(int group_id) {
        subgroupRepository.deleteByGroupId(group_id);
    }

    @Override
    public List<Subgroup> getAll() {
        return subgroupRepository.findAll();
    }
}
