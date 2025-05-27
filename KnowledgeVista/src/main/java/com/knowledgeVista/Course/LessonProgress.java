package com.knowledgeVista.Course;

import java.time.LocalDateTime;

import com.knowledgeVista.User.Muser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
	    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "lesson_id"})
	)
public class LessonProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Muser user;

    @ManyToOne
    private videoLessons lesson;

    @ManyToOne
    private CourseDetail course;

    private Double progress = 0.0;
    private boolean isCompleted = false;
    private LocalDateTime lastAccessed = LocalDateTime.now();

    public void updateProgress(double percentWatched) {
        this.progress = percentWatched;
        this.lastAccessed = LocalDateTime.now();
        if (percentWatched >= 95.0) {
            this.isCompleted = true;
        }
    }
}
