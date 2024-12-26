package io.hhplus.lecture.unit;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.LectureSchdule.LectureScheduleService;
import io.hhplus.lecture.unit.domain.builder.LectureScheduleTestDataBuilder;
import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import io.hhplus.lecture.interfaces.api.dto.response.LectureScheduleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LectureScheduleUnitTest {

    @InjectMocks
    private LectureScheduleService lectureScheduleService;

    @Mock
    private LectureScheduleRepositoryImpl lectureScheduleRepository;

    @Nested
    @DisplayName("특강 목록 조회 단위 테스트")
    class LectureScheduleTest {
        @Test
        void 날짜별_신청가능_특강목록을_조회한다() {
            // given
            LocalDate targetDate = LocalDate.now();
            List<LectureSchedule> schedules = List.of(
                    new LectureScheduleTestDataBuilder()
                            .withDate(targetDate)
                            .withStartTime(LocalTime.now().plusHours(1))
                            .withCurrentCapacity(0)
                            .build(),
                    new LectureScheduleTestDataBuilder()
                            .withDate(targetDate)
                            .withStartTime(LocalTime.now().plusHours(1))
                            .withCurrentCapacity(15)
                            .build()
            );

            given(lectureScheduleRepository.findByDate(targetDate))
                    .willReturn(schedules);

            // when
            List<LectureScheduleResponse> result = lectureScheduleService.getAvailableLectures(targetDate);

            // then
            assertEquals(2, result.size());
        }

        @Test
        void 정원이_초과된_특강은_목록에서_제외된다() {
            // given
            LocalDate targetDate = LocalDate.now();
            List<LectureSchedule> schedules = List.of(
                    new LectureScheduleTestDataBuilder()
                            .withDate(targetDate)
                            .withStartTime(LocalTime.now().plusHours(1))
                            .asFullyBooked()
                            .build(),
                    new LectureScheduleTestDataBuilder()
                            .withDate(targetDate)
                            .withStartTime(LocalTime.now().plusHours(1))
                            .withCurrentCapacity(0)
                            .build()
            );

            given(lectureScheduleRepository.findByDate(targetDate))
                    .willReturn(schedules);

            // when
            List<LectureScheduleResponse> result = lectureScheduleService.getAvailableLectures(targetDate);

            // then
            assertEquals(1, result.size());
        }

        @Test
        void 이미_시작된_특강은_목록에서_제외된다() {
            // given
            LocalDate today = LocalDate.now();
            List<LectureSchedule> schedules = List.of(
                    new LectureScheduleTestDataBuilder()
                            .withDate(today)
                            .withStartTime(LocalTime.now().minusHours(1))
                            .build(),
                    new LectureScheduleTestDataBuilder()
                            .withDate(today)
                            .withStartTime(LocalTime.now().plusHours(1))
                            .build()
            );

            given(lectureScheduleRepository.findByDate(today))
                    .willReturn(schedules);

            // when
            List<LectureScheduleResponse> result = lectureScheduleService.getAvailableLectures(today);

            // then
            assertEquals(1, result.size());
        }

        @Test
        void 해당_날짜에_특강이_없으면_빈_목록이_반환된다() {
            // given
            LocalDate targetDate = LocalDate.now();
            given(lectureScheduleRepository.findByDate(targetDate))
                    .willReturn(Collections.emptyList());

            // when
            List<LectureScheduleResponse> result = lectureScheduleService.getAvailableLectures(targetDate);

            // then
            assertTrue(result.isEmpty());
        }
    }
}
