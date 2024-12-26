package io.hhplus.lecture.interfaces.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record LectureApplyResponse(
        Long lectureId,
        String lectureName,
        String lecturer
) {

}