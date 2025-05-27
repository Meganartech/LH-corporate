package com.knowledgeVista.Course.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Course.LessonProgress;
import com.knowledgeVista.Course.videoLessons;
import com.knowledgeVista.User.Muser;

import java.util.List;
import java.util.Optional;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
	 Optional<LessonProgress> findByUserAndLesson(Muser user, videoLessons lesson);
	    List<LessonProgress> findByUserAndCourse(Muser user, CourseDetail course);

    @Query("SELECT lp.lesson.lessonId FROM LessonProgress lp WHERE lp.user.id = :userId AND lp.course.id = :courseId AND lp.isCompleted = true")
    List<Long> findCompletedLessonIdsByUserAndCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

}
