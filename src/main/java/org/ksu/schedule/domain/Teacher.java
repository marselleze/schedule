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
@Table(name = "teacher", uniqueConstraints = { @UniqueConstraint(columnNames = {"name", "post"}) })
public class Teacher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "post")
    private String post;

    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public Teacher(int id, String name, String post, Photo photo) {
        this.id = id;
        this.name = name;
        this.post = post;
        this.photo = photo;
    }
}
