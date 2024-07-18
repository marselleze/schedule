package org.ksu.schedule.rest.controller;

import org.ksu.schedule.domain.Subgroup;
import org.ksu.schedule.rest.dto.SubgroupDto;
import org.ksu.schedule.service.SubgroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления подгруппами.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SubgroupController {

    private final SubgroupService subgroupService;

    /**
     * Получить все подгруппы.
     *
     * @return список всех подгрупп в виде DTO.
     */
    @GetMapping("/subgroup")
    public List<SubgroupDto> getAllSubgroups() {
        List<SubgroupDto> subgroupDtoList = subgroupService.getAll()
                .stream()
                .map(SubgroupDto::toDto)
                .toList();
        return subgroupDtoList;
    }

    /**
     * Получить подгруппы по идентификатору группы.
     *
     * @param group_id идентификатор группы.
     * @return список подгрупп в виде DTO.
     */
    @GetMapping("/subgroup/groupId/{group_id}")
    public List<SubgroupDto> getSubgroupsByGroupId(@PathVariable int group_id) {
        List<SubgroupDto> subgroupDtoList = subgroupService.getByGroupId(group_id)
                .stream()
                .map(SubgroupDto::toDto)
                .toList();
        return subgroupDtoList;
    }

    /**
     * Получить подгруппы по номеру группы.
     *
     * @param group_number номер группы.
     * @return список подгрупп в виде DTO.
     */
    @GetMapping("/subgroup/groupNumber/{group_number}")
    public List<SubgroupDto> getSubgroupsByGroupNumber(@PathVariable String group_number) {
        List<SubgroupDto> subgroupDtoList = subgroupService.getByGroupNumber(group_number)
                .stream()
                .map(SubgroupDto::toDto)
                .toList();
        return subgroupDtoList;
    }

    /**
     * Вставить новую подгруппу.
     *
     * @param id идентификатор подгруппы.
     * @param number номер подгруппы.
     * @param group_number номер группы.
     * @return DTO новой подгруппы.
     */
    @PostMapping("/subgroup")
    public SubgroupDto insertSubgroup(@RequestParam int id, @RequestParam String number, @RequestParam String group_number) {
        Subgroup subgroup = subgroupService.insert(id, number, group_number);
        return SubgroupDto.toDto(subgroup);
    }

    /**
     * Обновить подгруппу.
     *
     * @param id идентификатор подгруппы.
     * @param number номер подгруппы.
     * @param group_number номер группы.
     * @return DTO обновленной подгруппы.
     */
    @PutMapping("/subgroup/{id}")
    public SubgroupDto updateSubgroup(@PathVariable int id, @RequestParam String number, @RequestParam String group_number) {
        Subgroup subgroup = subgroupService.update(id, number, group_number);
        return SubgroupDto.toDto(subgroup);
    }

    /**
     * Удалить подгруппу по идентификатору.
     *
     * @param id идентификатор подгруппы.
     */
    @DeleteMapping("/subgroup/{id}")
    public void deleteSubgroup(@PathVariable int id) {
        subgroupService.deleteById(id);
    }
}
