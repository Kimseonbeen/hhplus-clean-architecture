package io.hhplus.lecture.domain.lectureApply;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.validator.LectureApplyValidator;
import io.hhplus.lecture.infrastructure.LectureApplyRepositoryImpl;
import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LectureApplyService {

    private final LectureScheduleRepositoryImpl lectureScheduleRepository;
    private final LectureApplyRepositoryImpl lectureApplyRepository;

    public void apply(long userId, long lectureScheduleId) {
        LectureSchedule lectureSchedule = lectureScheduleRepository.findById(lectureScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강입니다."));

        // 수강 신청 검증
        //lectureSchedule.validateAvailableToApply();

        LectureApplyValidator.validateApply(lectureSchedule, userId, lectureApplyRepository);

        lectureSchedule.increaseCurrentCapacity();

        LectureApply lectureApply = LectureApply.apply(userId, lectureSchedule);
        lectureApplyRepository.save(lectureApply);
    }
}
