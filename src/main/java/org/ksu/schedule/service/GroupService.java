package org.ksu.schedule.service;

import org.ksu.schedule.domain.Group;

import java.util.List;

public interface GroupService {

    Group addGroup(Group group);

    void deleteById (int id);

    Group getByNumber(int number);

    List<Group> getByDirection(String direction);

    List<Group> getByProfile(String profile);

    Group updateGroup(int id, int number, String direction, String profile);

    List<Group> getAll();
}
