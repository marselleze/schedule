package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация интерфейса {@link TeacherService}.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    /**
     * Вставляет нового преподавателя.
     *
     * @param teacher объект преподавателя
     * @return сохраненный преподаватель
     */
    @Override
    public Teacher insert(Teacher teacher) {
        return teacherRepository.saveAndFlush(teacher);
    }

    /**
     * Обновляет информацию о преподавателе.
     *
     * @param id идентификатор преподавателя
     * @param teacherName имя преподавателя
     * @param post должность преподавателя
     * @return обновленный преподаватель
     */
    @Override
    public Teacher update(int id, String teacherName, String post) {
        return teacherRepository.saveAndFlush(Teacher.builder()
                .id(id)
                .name(teacherName)
                .post(post)
                .build());
    }

    /**
     * Получает преподавателя по имени.
     *
     * @param teacherName имя преподавателя
     * @return найденный преподаватель
     */
    @Override
    public Teacher getByName(String teacherName) {
        return teacherRepository.findByName(teacherName);
    }

    /**
     * Получает преподавателей по должности.
     *
     * @param post должность преподавателя
     * @return список преподавателей
     */
    @Override
    public List<Teacher> getByPost(String post) {
        return teacherRepository.findByPost(post);
    }

    /**
     * Удаляет преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя
     */
    @Override
    public void deleteById(int id) {
        teacherRepository.deleteById(id);
    }

    /**
     * Удаляет преподавателя по имени.
     *
     * @param teacherName имя преподавателя
     */
    @Override
    public void deleteByName(String teacherName) {
        teacherRepository.deleteByName(teacherName);
    }

    /**
     * Удаляет преподавателей по должности.
     *
     * @param post должность преподавателя
     */
    @Override
    public void deleteByPost(String post) {
        teacherRepository.deleteByPost(post);
    }

    /**
     * Получает всех преподавателей.
     *
     * @return список всех преподавателей
     */
    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    /**
     * Получает преподавателя по идентификатору.
     *
     * @param id идентификатор преподавателя
     * @return найденный преподаватель или null
     */
    @Override
    public Teacher getById(int id) {
        return teacherRepository.findById(id).orElse(null);
    }
}
