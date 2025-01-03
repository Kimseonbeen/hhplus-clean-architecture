package io.hhplus.lecture.domain.LectureSchdule;

import io.hhplus.lecture.interfaces.api.dto.response.LectureScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureScheduleService {

    private final LectureScheduleRepository lectureScheduleRepository;

    public List<LectureScheduleResponse> getAvailableLectures(LocalDate localDate) {

        List<LectureScheduleResponse> lectureSchedules = lectureScheduleRepository.findByDate(localDate)
                .stream()
                .filter(schedule -> schedule.getCurrentCapacity() < schedule.getCapacity())  // 현재 수강인원이 최대 정원보다 작은 경우만 필터링
                .filter(schedule -> LocalDateTime.now().isBefore(   //현재 시간이 특강 시작 시간보다 이전만 필터링
                        LocalDateTime.of(schedule.getDate(), schedule.getStartTime())
                ))
                .map(LectureScheduleResponse::from)  // LectureScheduleResponse로 변환
                .toList();

        return lectureSchedules;
    }
}
