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
	    uniqueConstraints = @UniqueConstraint(columnNames = {"user_user_id", "course_course_id"})
	)
public class CourseCompletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Muser user;

    @ManyToOne
    private CourseDetail course;

    private Double progressPercent = 0.0;
    private Integer lessonsCompleted = 0;
    private boolean isCompleted = false;
    private LocalDateTime lastAccessed = LocalDateTime.now();
    private LocalDateTime completedOn;

    public CourseCompletion(Muser user, CourseDetail course, LocalDateTime lastAccessed) {
        this.user = user;
        this.course = course;
        this.lastAccessed = lastAccessed != null ? lastAccessed : LocalDateTime.now();
    }

    public void updateCourseProgress(int totalLessons, int completedLessons) {
        if (totalLessons > 0) {
            this.lessonsCompleted = completedLessons;
            this.progressPercent = (completedLessons * 100.0) / totalLessons;
            this.lastAccessed = LocalDateTime.now();
            if (progressPercent >= 99.0) {
                this.isCompleted = true;
                this.completedOn = LocalDateTime.now();
            }
        }
    }
}
