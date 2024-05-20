package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    public Group getByNumber(String number) {
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
    public Group updateGroup(int id, String number, String direction, String profile) {

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
