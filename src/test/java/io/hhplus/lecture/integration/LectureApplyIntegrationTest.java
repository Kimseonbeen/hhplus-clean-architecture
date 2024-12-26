package io.hhplus.lecture.integration;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LectureApplyIntegrationTest {

    @Autowired
    private LectureApplyService lectureApplyService;

    @Autowired
    private LectureJpaApplyRepository lectureApplyRepository;
    @Autowired
    private LectureJpaScheduleRepository lectureScheduleRepository;

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
}
