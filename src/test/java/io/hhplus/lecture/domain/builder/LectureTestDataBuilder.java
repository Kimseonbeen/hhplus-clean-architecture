package io.hhplus.lecture.domain.builder;

import io.hhplus.lecture.domain.lecture.Lecture;

public class LectureTestDataBuilder {

    private static final String DEFAULT_NAME = "테스트 특강";
    private static final String DEFAULT_LECTURER = "강사님";
    private static final String DEFAULT_DESCRIPTION = "테스트";

    private String name = DEFAULT_NAME;
    private String lecturer = DEFAULT_LECTURER;
    private String description = DEFAULT_DESCRIPTION;

    public static Lecture defaultLecture() {
        return new LectureTestDataBuilder().build();
    }

    public Lecture build() {
        return Lecture.builder()
                .name(name)
                .lecturer(lecturer)
                .description(description)
                .build();
    }
}
