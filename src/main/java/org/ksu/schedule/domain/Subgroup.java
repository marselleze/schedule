package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subgroup")
public class Subgroup{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sub_number")
    private String number;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Subgroup setSubgroup(Subgroup subgroup) {
        this.setId(subgroup.getId());
        this.setNumber(subgroup.getNumber());
        this.setGroup(subgroup.getGroup());
        return this;
    }
}
