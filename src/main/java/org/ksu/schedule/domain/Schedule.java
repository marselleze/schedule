package org.ksu.schedule.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parity")
    private String parity;

    @ManyToOne(targetEntity = Subgroup.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subgroup_id")
    private Subgroup subgroup;

    @ManyToOne(targetEntity = Subject.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(targetEntity = Teacher.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(name = "day_week")
    private String dayWeek;

    @Column(name = "time_Start")
    private String timeStart;

    @Column(name = "time_End")
    private String timeEnd;

    @Column(name = "classroom")
    private String classroom;

    public void setDay(String day) {
        this.dayWeek = day;
    }

    public void setStartTime(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setEndTime(String endTime) {
        this.timeEnd = endTime;
    }

    public void setWeekView(String weekView) {
        this.parity = weekView;
    }




    public Schedule build() {
        return new Schedule(this.id, this.parity, this.subgroup, this.subject, this.teacher, this.dayWeek, this.timeStart, this.timeEnd, this.classroom);
    }
}
