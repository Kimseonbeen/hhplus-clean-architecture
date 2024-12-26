package io.hhplus.lecture.domain.lecture;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lecturer;

    private String description;

    @OneToMany(mappedBy = "lecture")
    private List<LectureSchedule> schedules = new ArrayList<>();
    
}
