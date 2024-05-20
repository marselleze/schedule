package org.ksu.schedule.repository;

import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubgroupRepository extends JpaRepository<Subgroup, Integer> {

    List<Subgroup> findByGroupId(int group_id);

    void deleteByGroupId(int group_id);

    Subgroup findByNumber(String number);

}
