package io.hhplus.lecture.domain.LectureSchdule;

import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureScheduleService {

    private final LectureScheduleRepositoryImpl lectureScheduleRepository;

    public List<LectureSchedule> getAvailableLectures(LocalDate localDate) {

        List<LectureSchedule> lectureSchedules = lectureScheduleRepository.findByDate(localDate)
                .stream()
                .filter(schedule -> schedule.getCurrentCapacity() < schedule.getCapacity())  // 현재 수강인원이 최대 정원보다 작은 경우만 필터링
                .filter(schedule -> LocalDateTime.now().isBefore(   //현재 시간이 특강 시작 시간보다 이전만 필터링
                        LocalDateTime.of(schedule.getDate(), schedule.getStartTime())
                ))
                .toList();

        return lectureSchedules;
    }
}
