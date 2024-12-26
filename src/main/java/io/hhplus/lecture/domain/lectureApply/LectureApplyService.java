package io.hhplus.lecture.domain.lectureApply;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.validator.LectureApplyValidator;
import io.hhplus.lecture.infrastructure.LectureApplyRepositoryImpl;
import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import io.hhplus.lecture.interfaces.api.dto.request.LectureApplyRequest;
import io.hhplus.lecture.interfaces.api.dto.response.LectureApplyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureApplyService {

    private final LectureScheduleRepositoryImpl lectureScheduleRepository;
    private final LectureApplyRepositoryImpl lectureApplyRepository;

    @Transactional
    public LectureApplyResponse apply(long userId, LectureApplyRequest request) {
        LectureSchedule lectureSchedule = lectureScheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 특강입니다."));

        LectureApplyValidator.validateApply(lectureSchedule, userId, lectureApplyRepository);

        lectureSchedule.increaseCurrentCapacity();

        LectureApply lectureApply = LectureApply.apply(userId, lectureSchedule);
        lectureApplyRepository.save(lectureApply);

        return new LectureApplyResponse(
                lectureSchedule.getLecture().getId(),
                lectureSchedule.getLecture().getName(),
                lectureSchedule.getLecture().getLecturer()
        );
    }

    @Transactional
    public List<LectureApplyResponse> getUserAppliedLectures(long userId) {

        List<LectureApply> lectureApplyList = lectureApplyRepository.findByUserId(userId);

        //각 항목은 특강 ID 및 이름, 강연자 정보를 담고 있어야 합니다.
        return lectureApplyList.stream()
                .map(apply -> new LectureApplyResponse(
                        apply.getLectureSchedule().getLecture().getId(),  // 특강 ID
                        apply.getLectureSchedule().getLecture().getName(),  // 특강 이름
                        apply.getLectureSchedule().getLecture().getLecturer()  // 강연자
                ))
                .toList();
    }
}
