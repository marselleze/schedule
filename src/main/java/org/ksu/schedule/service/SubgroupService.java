package org.ksu.schedule.service;

import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.List;

public interface SubgroupService {

    Subgroup insert(
            int id,
            int number,
            int group_id
    );

    Subgroup update(int id, int number, int group_id);

    Subgroup getByNumber(int number);

    List<Subgroup> getByGroupId(int group_id);

    void deleteById(int id);

    void deleteByGroupId(int group_id);

    List<Subgroup> getAll();

}
