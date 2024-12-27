package io.hhplus.lecture.integration;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import io.hhplus.lecture.domain.LectureSchdule.LectureScheduleRepository;
import io.hhplus.lecture.domain.lectureApply.LectureApplyRepository;
import io.hhplus.lecture.domain.lectureApply.LectureApplyService;
import io.hhplus.lecture.infrastructure.LectureJpaApplyRepository;
import io.hhplus.lecture.infrastructure.LectureJpaScheduleRepository;
import io.hhplus.lecture.interfaces.api.dto.request.LectureApplyRequest;
import io.hhplus.lecture.interfaces.api.dto.response.LectureApplyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LectureApplyIntegrationTest {

    @Autowired
    private LectureApplyService lectureApplyService;

    @Autowired
    private LectureApplyRepository lectureApplyRepository;
    @Autowired
    private LectureScheduleRepository lectureScheduleRepository;

    @Test
    @Transactional
    void 특강_신청이_성공적으로_처리된다() {
        // given
        final long USER_ID = 1L;
        LectureApplyRequest request = new LectureApplyRequest(1L);

        // when
        LectureApplyResponse response = lectureApplyService.apply(USER_ID, request);

        // then
        assertNotNull(response);
        assertEquals("spring start", response.lectureName());
    }

    @Test
    @Transactional
    void 특강_신청_목록이_정상적으로_조회된다() {
        // given
        final long USER_ID = 1L;
        LectureApplyRequest request = new LectureApplyRequest(1L);

        // 특강을 등록한다.
        lectureApplyService.apply(USER_ID, request);

        // when
        List<LectureApplyResponse> responses = lectureApplyService.getUserAppliedLectures(USER_ID);

        // then
        assertNotNull(responses);

        LectureApplyResponse response = responses.get(0);
        // 각 필드가 예상한 값과 일치하는지 검증
        assertAll(
                () -> assertEquals("spring start", response.lectureName()),
                () -> assertEquals("kim", response.lecturer()),
                () -> assertNotNull(response.lectureId())
        );
    }

    @Test
    void 동시에_40명이_신청시_30명만_성공() throws InterruptedException {
        // given
        LectureSchedule lectureSchedule = lectureScheduleRepository.findById(1L).get(); // 이미 있는 schedule 사용

        int threads = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < threads; i++) {

            long userId = i + 1;
            executorService.submit(() -> {
                try {
                    LectureApplyRequest request = new LectureApplyRequest(lectureSchedule.getId());
                    lectureApplyService.apply(userId, request);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(10, TimeUnit.SECONDS);

        // then
        LectureSchedule updatedSchedule = lectureScheduleRepository.findById(1L).get();
        assertAll(
                () -> assertEquals(30, successCount.get(), "성공한 신청 수는 30이어야 합니다"),
                () -> assertEquals(10, failCount.get(), "실패한 신청 수는 10이어야 합니다"),
                () -> assertEquals(30, updatedSchedule.getCurrentCapacity(), "현재 수강 인원은 30이어야 합니다")
        );
    }

    @Test
    void 동일한_유저는_같은_강의를_한번만_신청할_수_있다() throws InterruptedException {
        // given
        int thread = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(thread);
        CountDownLatch latch = new CountDownLatch(thread);

        final long USER_ID = 1L;
        final long LECTURE_SCHEDULE_ID = 1L;
        LectureApplyRequest request = new LectureApplyRequest(USER_ID);

        // when
        for (int i = 0; i < thread; i++) {
            executorService.submit(() -> {
                try {
                    lectureApplyService.apply(LECTURE_SCHEDULE_ID, request);
                } catch (Exception e) {
                    e.getStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        List<LectureApplyResponse> applies = lectureApplyService.getUserAppliedLectures(USER_ID);

        assertThat(applies).hasSize(1);
    }
}
