package io.hhplus.lecture.domain.builder;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.lecture.Lecture;

import java.time.LocalDate;
import java.time.LocalTime;

public class LectureScheduleTestDataBuilder {
    private static final int DEFAULT_CAPACITY = 30;

    private Long id = 1L;
    private Lecture lecture = LectureTestDataBuilder.defaultLecture();
    private LocalDate date = LocalDate.now();
    private LocalTime startTime = LocalTime.of(14, 0);
    private LocalTime endTime = LocalTime.of(15, 0);
    private int capacity = DEFAULT_CAPACITY;
    private int currentCapacity = 0;

    public LectureScheduleTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LectureScheduleTestDataBuilder withCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
        return this;
    }

    public LectureScheduleTestDataBuilder withDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LectureScheduleTestDataBuilder withStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LectureScheduleTestDataBuilder withLecture(Lecture lecture) {
        this.lecture = lecture;
        return this;
    }

    public LectureScheduleTestDataBuilder asFullyBooked() {
        this.currentCapacity = 30;
        return this;
    }

    public static LectureSchedule defaultSchedule() {
        return new LectureScheduleTestDataBuilder().build();
    }

    public LectureSchedule build() {
        return LectureSchedule.builder()
                .id(id)
                .lecture(lecture)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .capacity(capacity)
                .currentCapacity(currentCapacity)
                .build();
    }
}
