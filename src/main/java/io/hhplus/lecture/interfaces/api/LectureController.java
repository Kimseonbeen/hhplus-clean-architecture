package io.hhplus.lecture.interfaces.api;

import io.hhplus.lecture.domain.LectureSchdule.LectureScheduleService;
import io.hhplus.lecture.domain.lectureApply.LectureApplyService;
import io.hhplus.lecture.interfaces.api.dto.request.LectureApplyRequest;
import io.hhplus.lecture.interfaces.api.dto.response.LectureApplyResponse;
import io.hhplus.lecture.interfaces.api.dto.response.LectureScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureApplyService lectureApplyService;
    private final LectureScheduleService lectureScheduleService;

    @PostMapping("/{userId}/apply")
    public ResponseEntity<LectureApplyResponse> applyLecture(
            @PathVariable long userId,
            @RequestBody LectureApplyRequest lectureApplyRequest
    ) {
        LectureApplyResponse response = lectureApplyService.apply(userId, lectureApplyRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<LectureScheduleResponse>> getAvailableLectures(
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        List<LectureScheduleResponse> availableLectures = lectureScheduleService.getAvailableLectures(date);
        return ResponseEntity.ok(availableLectures);
    }

    @GetMapping("/{userId}/applies")
    public ResponseEntity<List<LectureApplyResponse>> getAppliedLectures(@PathVariable long userId) {
        List<LectureApplyResponse> userAppliedLectures = lectureApplyService.getUserAppliedLectures(userId);
        return ResponseEntity.ok(userAppliedLectures);
    }
}
