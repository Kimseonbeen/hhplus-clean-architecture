package io.hhplus.lecture.domain;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.builder.LectureScheduleTestDataBuilder;
import io.hhplus.lecture.domain.lectureApply.LectureApply;
import io.hhplus.lecture.domain.lectureApply.LectureApplyService;
import io.hhplus.lecture.infrastructure.LectureApplyRepositoryImpl;
import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class LectureApplyUnitTest {

    @InjectMocks
    private LectureApplyService lectureApplyService;

    @Mock
    private LectureScheduleRepositoryImpl lectureScheduleRepository;

    @Mock
    private LectureApplyRepositoryImpl lectureApplyRepository;

    @Nested
    @DisplayName("특강 신청 단위 테스트")
    class LectureApplyTest {

        @Test
        void 존재하지_않는_특강_스케줄에_대한_신청은_실패한다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 999L;

            given(lectureScheduleRepository.findById(SCHEDULE_ID))
                    .willReturn(Optional.empty());

            // when
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                lectureApplyService.apply(USER_ID, SCHEDULE_ID);
            });

            // then
            assertEquals("존재하지 않는 특강입니다.", exception.getMessage());
        }

        @Test
        void 수강_신청_시_수강인원이_30명_이상일_경우_요청은_실패한다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .asFullyBooked()
                    .build();

            given(lectureScheduleRepository.findById(USER_ID))
                    .willReturn(Optional.of(schedule));

            // when
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                lectureApplyService.apply(USER_ID, SCHEDULE_ID);
            });
            // then
            assertEquals("수강 신청이 마감되었습니다.", exception.getMessage());
        }

        @Test
        void 수강인원이_29명일때_신청이_성공한다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .withDate(LocalDate.now().plusDays(1))
                    .withCurrentCapacity(29)    // 현재 신청인원을 29으로 설정
                    .build();

            given(lectureScheduleRepository.findById(SCHEDULE_ID))
                    .willReturn(Optional.of(schedule));

            // when & then
            assertDoesNotThrow(() -> lectureApplyService.apply(USER_ID, SCHEDULE_ID));
        }

        @Test
        void 수강_신청_시_신청_시간이_특강_시작_이후_이라면_요청은_실패한다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .withDate(LocalDate.now().minusDays(1))
                    .build();

            given(lectureScheduleRepository.findById(USER_ID))
                    .willReturn(Optional.of(schedule));

            // when
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                lectureApplyService.apply(USER_ID, SCHEDULE_ID);
            });
            // then
            assertEquals("신청 가능한 시간이 지났습니다.", exception.getMessage());
        }

        @Test
        void 동일한_신청자는_동일한_강의_대해_한_번_수강_신청만_가능하다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .withDate(LocalDate.now().plusDays(1))
                    .build();

            given(lectureScheduleRepository.findById(USER_ID))
                    .willReturn(Optional.of(schedule));
            given(lectureApplyRepository.existsByUserIdAndLectureScheduleId(USER_ID, SCHEDULE_ID))
                    .willReturn(true);

            // when
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                lectureApplyService.apply(USER_ID, SCHEDULE_ID);
            });
            // then
            assertEquals("이미 신청한 특강입니다.", exception.getMessage());
        }

        @Test
        void 수강_신청_성공_시_현재_수강인원이_증가한다() {
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .withCurrentCapacity(0) // 현재 신청인원을 0으로 설정
                    .withDate(LocalDate.now().plusDays(1))
                    .build();

            given(lectureScheduleRepository.findById(1L))
                    .willReturn(Optional.of(schedule));

            // when
            lectureApplyService.apply(USER_ID, SCHEDULE_ID);
            // then
            assertEquals(1, schedule.getCurrentCapacity());
        }

        @Test
        void 수강_신청이_성공적으로_처리된다() {
            // given
            final long USER_ID = 1L;
            final long SCHEDULE_ID = 1L;

            LectureSchedule schedule = new LectureScheduleTestDataBuilder()
                    .withCurrentCapacity(0)     // 현재 수강 인원 0명
                    .build();

            given(lectureScheduleRepository.findById(SCHEDULE_ID))
                    .willReturn(Optional.of(schedule));
            given(lectureApplyRepository.existsByUserIdAndLectureScheduleId(USER_ID, SCHEDULE_ID))
                    .willReturn(false);         // 중복 신청 아님

            // when
            lectureApplyService.apply(USER_ID, SCHEDULE_ID);

            // then
            then(lectureApplyRepository).should().save(any(LectureApply.class));
            assertEquals(1, schedule.getCurrentCapacity());
        }
    }
}