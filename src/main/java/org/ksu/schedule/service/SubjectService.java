package org.ksu.schedule.service;

import org.ksu.schedule.domain.Subject;

import java.util.List;

public interface SubjectService {

    Subject insert(Subject subject);

    Subject update(int id,String nameSubject, String type);

    List<Subject> getByName(String nameSubject);

    List<Subject> getByType(String type);

    void deleteByName(String nameSubject);

    void deleteByType(String type);

    void deleteById(int id);

    List<Subject> getAll();

}
