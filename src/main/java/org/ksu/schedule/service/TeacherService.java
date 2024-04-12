package org.ksu.schedule.service;

import org.ksu.schedule.domain.Teacher;

import java.util.List;

public interface TeacherService {

    Teacher insert(Teacher teacher);

    Teacher update(int id, String teacherName, String post);

    Teacher getByName(String teacherName);

    List<Teacher> getByPost(String post);

    void deleteById(int id);

    void deleteByName(String teacherName);

    void deleteByPost(String post);

    List<Teacher> getAll();


}
