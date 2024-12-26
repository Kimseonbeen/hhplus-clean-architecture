package io.hhplus.lecture.domain.builder;

import io.hhplus.lecture.domain.lecture.Lecture;

import java.time.LocalTime;

public class LectureTestDataBuilder {

    private static final String DEFAULT_NAME = "테스트 특강";
    private static final String DEFAULT_LECTURER = "강사님";
    private static final String DEFAULT_DESCRIPTION = "테스트";
    private static final Long DEFAULT_ID = 1L;

    private Long id = DEFAULT_ID;
    private String name = DEFAULT_NAME;
    private String lecturer = DEFAULT_LECTURER;
    private String description = DEFAULT_DESCRIPTION;

    public static Lecture defaultLecture() {
        return new LectureTestDataBuilder().build();
    }

    public LectureTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LectureTestDataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LectureTestDataBuilder withLecturer(String lecturer) {
        this.lecturer = lecturer;
        return this;
    }

    public LectureTestDataBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Lecture build() {
        return Lecture.builder()
                .id(id)
                .name(name)
                .lecturer(lecturer)
                .description(description)
                .build();
    }
}
