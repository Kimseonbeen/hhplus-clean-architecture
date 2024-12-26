package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LectureJpaScheduleRepository extends JpaRepository<LectureSchedule, Long> {
    List<LectureSchedule> findByDate(LocalDate localDate);
}
