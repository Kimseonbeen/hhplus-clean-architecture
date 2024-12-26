package io.hhplus.lecture.integration;

import io.hhplus.lecture.domain.LectureSchdule.LectureScheduleService;
import io.hhplus.lecture.interfaces.api.dto.response.LectureScheduleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LectureScheduleIntegrationTest {

    @Autowired
    private LectureScheduleService lectureScheduleService;

    @Test
    @Transactional
    void 특강_목록_조회가_성공_한다() {
        // given

        // 테스트 데이터 lectureSchedule +Day 1 처리를 했음
        LocalDate targetDate = LocalDate.now().plusDays(1);

        // when
        List<LectureScheduleResponse> responses = lectureScheduleService.getAvailableLectures(targetDate);

        // then
        LectureScheduleResponse response = responses.get(0);
        assertAll(
                () -> assertEquals(1L, response.scheduleId()),
                () -> assertEquals(1L, response.lectureId()),
                () -> assertEquals("spring start", response.lectureName()),
                () -> assertEquals("kim", response.lecturer()),
                () -> assertEquals(LocalTime.of(22,0), response.startTime()),
                () -> assertEquals(LocalTime.of(23,0), response.endTime()),
                () -> assertNotNull(response.lectureId())
        );
        System.out.println("availableLectures = " + responses);
    }

}
