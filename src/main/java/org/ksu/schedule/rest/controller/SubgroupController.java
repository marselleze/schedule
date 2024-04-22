package org.ksu.schedule.rest.controller;

import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.rest.dto.SubgroupDto;
import org.ksu.schedule.service.SubgroupService;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SubgroupController {

    private final SubgroupService subgroupService;

    @GetMapping("/subgroup")
    public List<SubgroupDto> getAllSubgroups() {

        List<SubgroupDto> subgroupDtoList = subgroupService.getAll()
                .stream()
                .map(SubgroupDto::toDto)
                .toList();

        return subgroupDtoList;
    }

    @GetMapping("/subgroup/{group_id}")
    public List<SubgroupDto> getSubgroupsByGroupId(@PathVariable int group_id) {
        List<SubgroupDto> subgroupDtoList = subgroupService.getByGroupId(group_id)
                .stream()
                .map(SubgroupDto::toDto)
                .toList();

        return subgroupDtoList;
    }

    @PostMapping("/subgroup")
    public SubgroupDto insertSubgroup(@RequestParam int id, @RequestParam String number, @RequestParam int group_id) {
        
        Subgroup subgroup = subgroupService.insert(id, number, group_id);

        return SubgroupDto.toDto(subgroup);
    }

    @PutMapping("/subgroup/{id}")
    public SubgroupDto updateSubgroup(@PathVariable int id, @RequestParam String number, @RequestParam int group_id) {

        Subgroup subgroup = subgroupService.update(id, number, group_id);

        return SubgroupDto.toDto(subgroup);
    }

    @DeleteMapping("/subgroup/{id}")
    public void deleteSubgroup(@PathVariable int id) {
        subgroupService.deleteById(id);
    }


}
