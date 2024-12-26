package io.hhplus.lecture.domain.lectureApply;

import java.util.List;

public interface LectureApplyRepository {
    LectureApply save(LectureApply lectureApply);
    boolean existsByUserIdAndLectureScheduleId(long userId, long lectureScheduleId);
    List<LectureApply> findByUserId(long userId);
}