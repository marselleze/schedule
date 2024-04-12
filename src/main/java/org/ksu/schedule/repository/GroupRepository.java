package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    // Метод кастомных запросов
    //@Query("select d from Group d where d.direction = :direction")
    //Group findByDirection(@Param("direction") String direction);


    //@Query("select p from Group p where p.profile = :profile")
    //Group findByProfile(@Param("profile") String profile);


    // Более современный метод создания собственных методов
    List<Group> findByDirection(String direction);
    List<Group> findByProfile(String profile);

    Group findByNumber(int number);

    //@Query("select d from Subgroup d where d.id = :subgroups")
    //Group findBySubgroupId(@Param("subgroups") int subgroups);;

    void deleteByNumber(int number);



}
