package io.hhplus.lecture.infrastructure;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.LectureSchdule.LectureScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LectureScheduleRepositoryImpl implements LectureScheduleRepository {

    private final LectureJpaScheduleRepository lectureJpaScheduleRepository;

    @Override
    public Optional<LectureSchedule> findById(Long id) {
        return lectureJpaScheduleRepository.findById(id);
    }
}
