package org.ksu.schedule.service;

import org.ksu.schedule.domain.Group;
import org.ksu.schedule.domain.Subgroup;

import java.util.Collection;
import java.util.List;

public interface SubgroupService {

    Subgroup insert(
            int id,
            String number,
            String group_number
    );

    Subgroup update(int id, String number, String group_number);

    Subgroup getByNumber(String number);

    List<Subgroup> getByGroupId(int group_id);

    void deleteById(int id);

    void deleteByGroupId(int group_id);

    List<Subgroup> getAll();

    List<Subgroup> getByGroupNumber(String groupNumber);

}
