package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * Класс, представляющий сущность группы в системе расписания.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "batch")
public class Group {

    /**
     * Уникальный идентификатор группы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Номер группы.
     */
    @Column(name = "number")
    private String number;

    /**
     * Направление группы.
     */
    @Column(name = "direction")
    private String direction;

    /**
     * Профиль группы.
     */
    @Column(name = "profile")
    private String profile;

    @ManyToOne(targetEntity = Faculty.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    //@OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Subgroup> subgroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(number, group.number) &&
                Objects.equals(direction, group.direction) &&
                Objects.equals(profile, group.profile) &&
                Objects.equals(faculty, group.faculty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, direction, profile, faculty);
    }

}
