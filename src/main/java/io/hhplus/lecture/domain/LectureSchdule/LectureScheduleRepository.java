package io.hhplus.lecture.domain.LectureSchdule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureScheduleRepository {
    Optional<LectureSchedule> findById(Long id);
    List<LectureSchedule> findByDate(LocalDate localDate);
}
