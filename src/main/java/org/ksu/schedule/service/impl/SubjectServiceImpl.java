package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.ksu.schedule.domain.Subject;
import org.ksu.schedule.repository.SubjectRepository;
import org.ksu.schedule.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реализация интерфейса {@link SubjectService}.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    /**
     * Вставляет новый предмет.
     *
     * @param subject объект предмета
     * @return сохраненный предмет
     */
    @Override
    public Subject insert(Subject subject) {
        return subjectRepository.saveAndFlush(subject);
    }

    /**
     * Обновляет предмет.
     *
     * @param id идентификатор предмета
     * @param nameSubject название предмета
     * @param type тип предмета
     * @return обновленный предмет
     */
    @Override
    public Subject update(int id, String nameSubject, String type) {
        return subjectRepository.saveAndFlush(
                Subject.builder()
                        .id(id)
                        .name(nameSubject)
                        .type(type)
                        .build());
    }

    /**
     * Получает предметы по названию.
     *
     * @param nameSubject название предмета
     * @return список предметов
     */
    @Override
    public List<Subject> getByName(String nameSubject) {
        return subjectRepository.findByName(nameSubject);
    }

    /**
     * Получает предметы по типу.
     *
     * @param type тип предмета
     * @return список предметов
     */
    @Override
    public List<Subject> getByType(String type) {
        return subjectRepository.findByType(type);
    }

    /**
     * Удаляет предмет по названию.
     *
     * @param nameSubject название предмета
     */
    @Override
    public void deleteByName(String nameSubject) {
        subjectRepository.deleteByName(nameSubject);
    }

    /**
     * Удаляет предмет по типу.
     *
     * @param type тип предмета
     */
    @Override
    public void deleteByType(String type) {
        subjectRepository.deleteByType(type);
    }

    /**
     * Удаляет предмет по идентификатору.
     *
     * @param id идентификатор предмета
     */
    @Override
    public void deleteById(int id) {
        subjectRepository.deleteById(id);
    }

    /**
     * Получает все предметы.
     *
     * @return список всех предметов
     */
    @Override
    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }
}
