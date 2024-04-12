package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.repository.SubjectRepository;
import org.ksu.schedule.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public Subject insert(Subject subject) {
        return subjectRepository.saveAndFlush(subject);
    }

    @Override
    public Subject update(int id, String nameSubject, String type) {
        return subjectRepository.saveAndFlush(
                Subject.builder()
                        .id(id)
                        .name(nameSubject)
                        .type(type)
                        .build());
    }

    @Override
    public List<Subject> getByName(String nameSubject) {
        return subjectRepository.findByName(nameSubject);
    }

    @Override
    public List<Subject> getByType(String type) {
        return subjectRepository.findByType(type);
    }

    @Override
    public void deleteByName(String nameSubject) {
        subjectRepository.deleteByName(nameSubject);
    }

    @Override
    public void deleteByType(String type) {
        subjectRepository.deleteByType(type);
    }

    @Override
    public void deleteById(int id) {
        subjectRepository.deleteById(id);
    }

    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }
}
