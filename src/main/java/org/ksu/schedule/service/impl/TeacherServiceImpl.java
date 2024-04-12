package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    @Override
    public Teacher insert(Teacher teacher) {
        return teacherRepository.saveAndFlush(teacher);
    }

    @Override
    public Teacher update(int id, String teacherName, String post) {
        return teacherRepository.saveAndFlush(Teacher.builder()
                .id(id)
                .name(teacherName)
                .post(post)
                .build());
    }

    @Override
    public Teacher getByName(String teacherName) {
        return teacherRepository.findByName(teacherName);
    }

    @Override
    public List<Teacher> getByPost(String post) {
        return teacherRepository.findByPost(post);
    }

    @Override
    public void deleteById(int id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String teacherName) {
        teacherRepository.deleteByName(teacherName);
    }

    @Override
    public void deleteByPost(String post) {
        teacherRepository.deleteByPost(post);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
}
