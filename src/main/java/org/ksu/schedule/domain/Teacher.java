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
@Table(name = "teacher", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "post"}) })
public class Teacher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "post")
    private String post;

    public Teacher(int id, String name, String post) {
        this.id = id;
        this.name = name;
        this.post = post;
    }


}
