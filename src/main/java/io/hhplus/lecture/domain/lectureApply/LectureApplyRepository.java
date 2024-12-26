package io.hhplus.lecture.domain.lectureApply;

public interface LectureApplyRepository {
    LectureApply save(LectureApply lectureApply);
    boolean existsByUserIdAndLectureScheduleId(long userId, long lectureScheduleId);
}