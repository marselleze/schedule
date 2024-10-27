package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.rest.dto.GroupDto;
import org.ksu.schedule.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления группами.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * Получение всех групп.
     *
     * @return список всех групп в формате GroupDto
     */
    @GetMapping("/batches")
    public List<GroupDto> getAllGroups() {
        return groupService.getAll()
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавление новой группы.
     *
     * @param groupDto данные новой группы
     * @return добавленная группа в формате GroupDto
     */
    @PostMapping("/batches")
    public GroupDto insertGroup(@RequestBody GroupDto groupDto) {
        Group group = groupService.addGroup(GroupDto.toDomain(groupDto));
        return GroupDto.toDto(group);
    }

    /**
     * Обновление данных группы.
     *
     * @param id        идентификатор группы
     * @param number    номер группы
     * @param direction направление группы
     * @param profile   профиль группы
     * @return обновленная группа в формате GroupDto
     */
    @PutMapping("/batches/{id}")
    public GroupDto updateGroup(@PathVariable int id,
                                @RequestParam String number,
                                @RequestParam String direction,
                                @RequestParam String profile) {
        Group group = groupService.updateGroup(id, number, direction, profile);
        return GroupDto.toDto(group);
    }

    /**
     * Удаление группы.
     *
     * @param id идентификатор группы
     */
    @DeleteMapping("/batches/{id}")
    public void deleteGroup(@PathVariable int id) {
        groupService.deleteById(id);
    }

    /**
     * Получение групп по направлению.
     *
     * @param direction направление группы
     * @return список групп в формате GroupDto
     */
    @GetMapping("/batches/direction/{direction}")
    public List<GroupDto> getGroupByDirection(@PathVariable String direction) {
        return groupService.getByDirection(direction)
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение групп по профилю.
     *
     * @param profile профиль группы
     * @return список групп в формате GroupDto
     */
    @GetMapping("/batches/profile/{profile}")
    public List<GroupDto> getGroupByProfile(@PathVariable String profile) {
        return groupService.getByProfile(profile)
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получение группы по номеру.
     *
     * @param number номер группы
     * @return группа в формате GroupDto
     */
    @GetMapping("/batches/number/{number}")
    public GroupDto getGroupByNumber(@PathVariable String number) {
        return GroupDto.toDto(groupService.getByNumber(number));
    }

    @GetMapping("/batches/faculty/name/{name}")
    public List<GroupDto> getGroupByFaculty(@PathVariable String name) {
        return groupService.getByFacultyName(name)
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }
}
