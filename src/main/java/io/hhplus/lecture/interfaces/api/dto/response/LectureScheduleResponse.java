package io.hhplus.lecture.interfaces.api.dto.response;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;

import java.time.LocalTime;

public record LectureScheduleResponse(
        Long scheduleId,
        Long lectureId,
        String lectureName,
        String lecturer,
        LocalTime startTime,
        LocalTime endTime,
        int capacity,
        int currentCapacity
) {
    public static LectureScheduleResponse from(LectureSchedule schedule) {
        return new LectureScheduleResponse(
                schedule.getId(),
                schedule.getLecture().getId(),
                schedule.getLecture().getName(),
                schedule.getLecture().getLecturer(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getCapacity(),
                schedule.getCurrentCapacity()
        );
    }
}