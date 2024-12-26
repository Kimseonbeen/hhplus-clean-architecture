package io.hhplus.lecture.unit.domain.builder;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.lectureApply.LectureApply;

import java.time.LocalDateTime;

public class LectureApplyTestDataBuilder {

    private static final long DEFAULT_USER_ID = 1L;

    private long id = 1L;
    private long userId = DEFAULT_USER_ID;
    private LectureSchedule lectureSchedule = LectureScheduleTestDataBuilder.defaultSchedule();
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public LectureApplyTestDataBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public LectureApplyTestDataBuilder withUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public LectureApplyTestDataBuilder withLectureSchedule(LectureSchedule lectureSchedule) {
        this.lectureSchedule = lectureSchedule;
        return this;
    }

    public LectureApply build() {
        return LectureApply.builder()
                .id(id)
                .userId(userId)
                .lectureSchedule(lectureSchedule)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
