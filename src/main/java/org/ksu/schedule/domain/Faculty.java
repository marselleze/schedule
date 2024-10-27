package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculty")
@Data
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "facultyName", nullable = false)
    private String facultyName;

    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

}