package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.lectureApply.LectureApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureJpaApplyRepository extends JpaRepository<LectureApply, Long> {
    boolean existsByUserIdAndLectureScheduleId(long userId, long lectureScheduleId);
    List<LectureApply> findByUserId(long userId);
}

