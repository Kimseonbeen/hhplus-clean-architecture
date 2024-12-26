package io.hhplus.lecture.domain;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.builder.LectureApplyTestDataBuilder;
import io.hhplus.lecture.domain.builder.LectureScheduleTestDataBuilder;
import io.hhplus.lecture.domain.builder.LectureTestDataBuilder;
import io.hhplus.lecture.domain.lectureApply.LectureApply;
import io.hhplus.lecture.domain.lectureApply.LectureApplyService;
import io.hhplus.lecture.infrastructure.LectureApplyRepositoryImpl;
import io.hhplus.lecture.infrastructure.LectureScheduleRepositoryImpl;
import io.hhplus.lecture.interfaces.api.LectureApplyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
                    .withDate(LocalDate.now().plusDays(1))
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

    @Nested
    @DisplayName("특강 신청 완료 목록 조회 단위 테스트")
    class lectureApplyComplete {

        // 1. userId에 등록된 특강이 없을 시
        // 2. 정상 목록 조회

        @Test
        void userId에_등록된_특강이_없으면_빈_리스트를_반환한다() {
            // given
            final long USER_ID = 1L;

            given(lectureApplyRepository.findByUserId(USER_ID))
                    .willReturn(List.of());
            // when
            List<LectureApplyResponse> result = lectureApplyService.getUserAppliedLectures(USER_ID);

            // then
            assertThat(result).isEmpty();

        }

        @Test
        @DisplayName("신청한 특강 목록을 정상적으로 조회한다")
        void returnAppliedLecturesList() {
            // given
            final long USER_ID = 1L;

            LectureSchedule schedule1 = new LectureScheduleTestDataBuilder()
                    .withId(1L)
                    .withLecture(new LectureTestDataBuilder()
                            .withId(1L)
                            .withName("특강1")
                            .withLecturer("강사1")
                            .build())
                    .build();

            LectureSchedule schedule2 = new LectureScheduleTestDataBuilder()
                    .withId(2L)
                    .withLecture(new LectureTestDataBuilder()
                            .withId(2L)
                            .withName("특강2")
                            .withLecturer("강사2")
                            .build())
                    .build();

            List<LectureApply> applies = List.of(
                    new LectureApplyTestDataBuilder()
                            .withId(1L)
                            .withUserId(USER_ID)
                            .withLectureSchedule(schedule1)
                            .build(),
                    new LectureApplyTestDataBuilder()
                            .withId(2L)
                            .withUserId(USER_ID)
                            .withLectureSchedule(schedule2)
                            .build()
            );

            given(lectureApplyRepository.findByUserId(USER_ID)).willReturn(applies);

            // when
            List<LectureApplyResponse> result = lectureApplyService.getUserAppliedLectures(USER_ID);

            // then
            assertEquals(2, result.size());

            // 첫 번째 항목 검증
            assertEquals(1L, result.get(0).lectureId());
            assertEquals("특강1", result.get(0).lectureName());
            assertEquals("강사1", result.get(0).lecturer());

            // 두 번째 항목 검증
            assertEquals(2L, result.get(1).lectureId());
            assertEquals("특강2", result.get(1).lectureName());
            assertEquals("강사2", result.get(1).lecturer());
        }
    }
}