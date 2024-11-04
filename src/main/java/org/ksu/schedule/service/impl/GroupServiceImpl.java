package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.FacultyRepository;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация интерфейса {@link GroupService}.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final FacultyRepository facultyRepository;

    /**
     * Добавляет новую группу.
     *
     * @param group группа для добавления
     * @return добавленная группа
     */
    @Override
    public Group addGroup(Group group) {
        return groupRepository.save(group);
    }

    /**
     * Удаляет группу по её ID.
     *
     * @param id ID группы для удаления
     */
    @Override
    public void deleteById(int id) {
        groupRepository.deleteById(id);
    }

    /**
     * Получает группу по её номеру.
     *
     * @param number номер группы
     * @return найденная группа
     */
    @Override
    public Group getByNumber(String number) {
        return groupRepository.findByNumber(number);
    }

    /**
     * Получает список групп по направлению.
     *
     * @param direction направление группы
     * @return список групп по заданному направлению
     */
    @Override
    public List<Group> getByDirection(String direction) {
        return groupRepository.findByDirection(direction);
    }

    /**
     * Получает список групп по профилю.
     *
     * @param profile профиль группы
     * @return список групп по заданному профилю
     */
    @Override
    public List<Group> getByProfile(String profile) {
        return groupRepository.findByProfile(profile);
    }

    /**
     * Обновляет информацию о группе.
     *
     * @param id        ID группы
     * @param number    номер группы
     * @param direction направление группы
     * @param profile   профиль группы
     * @return обновленная группа
     */
    @Override
    public Group updateGroup(int id, String number, String direction, String profile, Integer facultyId) {
        Group group = Group.builder()
                .id(id)
                .number(number)
                .direction(direction)
                .profile(profile)
                .faculty(facultyRepository.findById(facultyId).isPresent() ? facultyRepository.findById(facultyId).get() : null)
                .build();

        return groupRepository.saveAndFlush(group);
    }

    /**
     * Получает список всех групп.
     *
     * @return список всех групп
     */
    @Override
    public List<Group> getAll() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> getByFacultyName(String facultyName) {
        return groupRepository.findByFaculty_FacultyName(facultyName);
    }

    @Override
    public List<Group> getByFacultyAbb(String facultyAbb) {
        return groupRepository.findByFaculty_Abbreviation(facultyAbb);
    }
}
