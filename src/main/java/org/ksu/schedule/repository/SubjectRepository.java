package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findByName(String nameSubject);

    Subject findOneByName(String nameSubject);

    Subject findOneById(int id);

    @Query ("SELECT DISTINCT s FROM Subject s WHERE  s.name = ?1 AND s.type =?2")
    Subject findByNameAndType(@Param("name") String nameSubject, @Param ("type") String type);

    List<Subject> findByType(String type);

    void deleteByName(String nameSubject);

    void deleteByType(String type);

    Long findIdByNameAndType(String name, String type);
}
