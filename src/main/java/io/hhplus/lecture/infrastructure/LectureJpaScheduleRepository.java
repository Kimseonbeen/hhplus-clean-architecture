package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureJpaScheduleRepository extends JpaRepository<LectureSchedule, Long> {
    List<LectureSchedule> findByDate(LocalDate localDate);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ls from LectureSchedule ls where ls.id = :id")
    Optional<LectureSchedule> findByIdWithLock(Long id);
}
