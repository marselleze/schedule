package org.ksu.schedule.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, представляющий сущность подгруппы в системе.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subgroup")
public class Subgroup {

    /**
     * Уникальный идентификатор подгруппы.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Номер подгруппы.
     */
    @Column(name = "sub_number")
    private String number;

    /**
     * Группа, к которой относится подгруппа.
     */
    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "size")
    private int size;

    //@OneToMany(mappedBy = "subgroup", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Schedule> schedules;


    /**
     * Устанавливает свойства подгруппы на основе другой подгруппы.
     *
     * @param subgroup объект {@link Subgroup}, из которого будут взяты данные
     * @return обновленный объект {@link Subgroup}
     */
    public Subgroup setSubgroup(Subgroup subgroup) {
        this.setId(subgroup.getId());
        this.setNumber(subgroup.getNumber());
        this.setGroup(subgroup.getGroup());
        return this;
    }
}
