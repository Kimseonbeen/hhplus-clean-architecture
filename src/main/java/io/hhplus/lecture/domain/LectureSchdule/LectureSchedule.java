package io.hhplus.lecture.domain.LectureSchdule;

import io.hhplus.lecture.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LectureSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private LocalDate date;

    private LocalTime startTime;

    private LocalTime endTime;

    private int capacity = 30;

    private int currentCapacity = 0;

    public void validateAvailableToApply() {
        validateCapacity();
        validateDateTime();
    }

    private void validateCapacity() {
        if (this.currentCapacity >= this.capacity) {
            throw new IllegalArgumentException("수강 신청이 마감되었습니다.");
        }
    }

    private void validateDateTime() {
        if (isExpired()) {
            throw new IllegalArgumentException("신청 가능한 시간이 지났습니다.");
        }
    }

    private boolean isExpired() {
        LocalDateTime lectureDateTime = LocalDateTime.of(date, startTime);
        return LocalDateTime.now().isAfter(lectureDateTime);
    }

    public void increaseCurrentCapacity() {
        this.currentCapacity++;
    }

}
