package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaScheduleRepository extends JpaRepository<LectureSchedule, Long> {

}
