package io.hhplus.lecture.domain.validator;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.infrastructure.LectureApplyRepositoryImpl;

public class LectureApplyValidator {
    public static void validateApply(LectureSchedule schedule, long userId, LectureApplyRepositoryImpl repository) {
        schedule.validateAvailableToApply();
        validateNotDuplicateApply(schedule.getId(), userId, repository);
    }

    private static void validateNotDuplicateApply(long scheduleId, long userId, LectureApplyRepositoryImpl repository) {
        if (repository.existsByUserIdAndLectureScheduleId(userId, scheduleId)) {
            throw new IllegalArgumentException("이미 신청한 특강입니다.");
        }
    }
}
