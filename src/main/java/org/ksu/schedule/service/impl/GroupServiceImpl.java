package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;

    @Override
    public Group addGroup(Group group) {


        return groupRepository.save(group);
    }

    @Override
    public void deleteById(int id) {
        groupRepository.deleteById(id);
    }

    @Override
    public Group getByNumber(int number) {
        return groupRepository.findByNumber(number);
    }

    @Override
    public List<Group> getByDirection(String direction) {
        return groupRepository.findByDirection(direction);
    }

    @Override
    public List<Group> getByProfile(String profile) {
        return groupRepository.findByProfile(profile);
    }

    @Override
    public Group updateGroup(int id, int number, String direction, String profile) {

        Group group = Group.builder()
                .id(id)
                .number(number)
                .direction(direction)
                .profile(profile)
                .build();

        return groupRepository.saveAndFlush(group);
    }



    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

}
