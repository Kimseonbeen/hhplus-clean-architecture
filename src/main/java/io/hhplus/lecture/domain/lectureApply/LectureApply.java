package io.hhplus.lecture.domain.lectureApply;

import io.hhplus.lecture.domain.LectureSchdule.LectureSchedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "lecture_apply", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "lecture_schedule_id"})
})
public class LectureApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_schedule_id")
    private LectureSchedule lectureSchedule;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private LectureApply(long id, long userId, LectureSchedule lectureSchedule, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.lectureSchedule = lectureSchedule;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 정적 팩토리 메서드
    public static LectureApply apply(Long userId, LectureSchedule lectureSchedule) {
        return LectureApply.builder()
                .userId(userId)
                .lectureSchedule(lectureSchedule)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
