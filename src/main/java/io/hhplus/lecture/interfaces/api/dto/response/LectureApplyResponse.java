package io.hhplus.lecture.interfaces.api.dto.response;

public record LectureApplyResponse(
        Long lectureId,
        String lectureName,
        String lecturer
) {

}