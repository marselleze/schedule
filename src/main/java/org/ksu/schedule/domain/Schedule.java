package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Класс, представляющий сущность расписания в системе.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule {

    /**
     * Уникальный идентификатор расписания.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Четность недели.
     */
    @Column(name = "parity")
    private String parity;

    /**
     * Подгруппа, к которой относится расписание.
     */
    @ManyToOne(targetEntity = Subgroup.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "subgroup_id")
    private Subgroup subgroup;

    /**
     * Предмет, включенный в расписание.
     */
    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    /**
     * Преподаватель, ведущий предмет.
     */
    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    /**
     * День недели.
     */
    @Column(name = "day_week")
    private String dayWeek;

    /**
     * Время начала занятия.
     */
    @Column(name = "time_start")
    private String timeStart;

    /**
     * Время окончания занятия.
     */
    @Column(name = "time_end")
    private String timeEnd;

    /**
     * Аудитория, в которой проходит занятие.
     */
    @Column(name = "classroom")
    private String classroom;

    /**
     * Создает объект расписания на основе текущего состояния.
     *
     * @return объект {@link Schedule}
     */
    public Schedule build() {
        return new Schedule(this.id, this.parity, this.subgroup, this.subject, this.teacher, this.dayWeek, this.timeStart, this.timeEnd, this.classroom);
    }
}
