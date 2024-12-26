package io.hhplus.lecture.domain.LectureSchdule;

import java.util.Optional;

public interface LectureScheduleRepository {
    Optional<LectureSchedule> findById(Long id);
}
