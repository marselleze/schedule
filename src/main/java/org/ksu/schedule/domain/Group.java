package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "number")
    private String number;

    //@OneToMany(mappedBy = "group")
    //private List<Subgroup> subgroups;

    @Column(name = "direction")
    private String direction;

    @Column(name = "profile")
    private String profile;


}
