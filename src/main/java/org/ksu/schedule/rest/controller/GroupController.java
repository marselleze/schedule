package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.rest.dto.GroupDto;
import org.ksu.schedule.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/batches")
    public List<GroupDto> getAllGroups() {
        return groupService.getAll()
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/batches")
    public GroupDto insertGroup(@RequestBody GroupDto groupDto) {

        Group group = groupService.addGroup(GroupDto.toDomain(groupDto));

        return GroupDto.toDto(group);
    }

    @PutMapping("/batches/{id}")
    public GroupDto updateGroup(@PathVariable int id,
                                @RequestParam int number,
                                @RequestParam String direction,
                                @RequestParam String profile) {
        Group group = groupService.updateGroup(id, number, direction, profile);

        return GroupDto.toDto(group);
    }

    @DeleteMapping("/batches/{id}")
    public void deleteGroup(@PathVariable int id) {

        groupService.deleteById(id);
    }

    @GetMapping("/batches/direction/{direction}")
    public List<GroupDto> getGroupByDirection(@PathVariable String direction){

        return groupService.getByDirection(direction)
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/batches/profile/{profile}")
    public List<GroupDto> getGroupByProfile(@PathVariable String profile){

        return groupService.getByProfile(profile)
                .stream()
                .map(GroupDto::toDto)
                .collect(Collectors.toList());
    }



}
