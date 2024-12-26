package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.lectureApply.LectureApply;
import io.hhplus.lecture.domain.lectureApply.LectureApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureApplyRepositoryImpl implements LectureApplyRepository {

    private final LectureJpaApplyRepository lectureJpaApplyRepository;

    @Override
    public LectureApply save(LectureApply lectureApply) {

        return lectureJpaApplyRepository.save(lectureApply);

    }

    @Override
    public boolean existsByUserIdAndLectureScheduleId(long userId, long lectureScheduleId) {
        return lectureJpaApplyRepository.existsByUserIdAndLectureScheduleId(userId, lectureScheduleId);
    }

    @Override
    public List<LectureApply> findByUserId(long userId) {
        return lectureJpaApplyRepository.findByUserId(userId);
    }
}
