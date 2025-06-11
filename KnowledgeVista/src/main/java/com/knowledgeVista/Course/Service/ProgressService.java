package com.knowledgeVista.Course.Service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.knowledgeVista.Course.CourseCompletion;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Course.LessonProgress;
import com.knowledgeVista.Course.videoLessons;
import com.knowledgeVista.Course.Repository.CourseCompletionRepository;
import com.knowledgeVista.Course.Repository.LessonProgressRepository;
import com.knowledgeVista.Course.Test.Repository.MusertestactivityRepo;
import com.knowledgeVista.User.Muser;

@Service
public class ProgressService {
	 @Autowired
	    private LessonProgressRepository lessonProgressRepo;

	    @Autowired
	    private CourseCompletionRepository courseCompletionRepo;
	    @Autowired
	    private MusertestactivityRepo muserTestActivityRepo;
@Async
public void updateLessonAndCourseProgress(Muser user, videoLessons lesson, double lessonPercent) {
    CourseDetail course = lesson.getCourseDetail();

    // 1. Update LessonProgress
    LessonProgress lessonProgress = lessonProgressRepo
            .findByUserAndLesson(user, lesson)
            .orElseGet(() -> {
                LessonProgress lp = new LessonProgress();
                lp.setUser(user);
                lp.setLesson(lesson);
                lp.setCourse(course);
                return lp;
            });

    if (lessonPercent > lessonProgress.getProgress()) {
        lessonProgress.updateProgress(lessonPercent);
    } else {
        lessonProgress.setLastAccessed(LocalDateTime.now());
    }
    lessonProgressRepo.save(lessonProgress);

    // 2. Course Completion Logic
    List<videoLessons> allLessons = course.getVideoLessons();
    int totalLessons = allLessons != null ? allLessons.size() : 0;

    // Check if test is mandatory AND completed
    boolean testCompleted = false;
    if (course.isTestMandatory()) {
        testCompleted = muserTestActivityRepo
                .findLatestByUserAndCourseNative(user.getUserId(), course.getCourseId())
                .isPresent();

        if (testCompleted) {
            totalLessons += 1; // Add test to total lessons only if completed
        }
    }

    // Count completed lessons
    long completedLessons = lessonProgressRepo
            .findByUserAndCourse(user, course).stream()
            .filter(LessonProgress::isCompleted)
            .count();

    if (testCompleted) {
        completedLessons += 1; // Add test to completed lessons only if completed
    }

    System.out.println("Total lessons (with test if completed): " + totalLessons);
    System.out.println("Completed lessons (with test if completed): " + completedLessons);

    CourseCompletion courseCompletion = courseCompletionRepo
            .findByUserAndCourse(user, course)
            .orElseGet(() -> new CourseCompletion(user, course, null));

    boolean updatedProgress = false;

    if (completedLessons > courseCompletion.getLessonsCompleted()) {
        courseCompletion.updateCourseProgress(totalLessons, (int) completedLessons);
        updatedProgress = true;
    }

    courseCompletion.setLastAccessed(LocalDateTime.now());

    boolean isAllLessonsCompleted = completedLessons == totalLessons;

    if (isAllLessonsCompleted && testCompleted && !courseCompletion.isCompleted()) {
        courseCompletion.setCompleted(true);
        courseCompletion.setCompletedOn(LocalDateTime.now());
        updatedProgress = true;
    }

    if (updatedProgress) {
        courseCompletionRepo.save(courseCompletion);
    }
}


@Async
public void updateCourseProgress(Muser user, CourseDetail course) {
    List<videoLessons> allLessons = course.getVideoLessons();
    int totalLessons = allLessons != null ? allLessons.size() : 0;

    // Check if test is mandatory AND completed
    boolean testCompleted = false;
    if (course.isTestMandatory()) {
        testCompleted = muserTestActivityRepo
                .findLatestByUserAndCourseNative(user.getUserId(), course.getCourseId())
                .isPresent();

        if (testCompleted) {
            totalLessons += 1; // Add test to total lessons only if completed
        }
    }

    long completedLessons = lessonProgressRepo
            .findByUserAndCourse(user, course).stream()
            .filter(LessonProgress::isCompleted)
            .count();

    if (testCompleted) {
        completedLessons += 1;  // Add test to completed lessons only if completed
    }

    System.out.println("Async updateCourseProgress - Total lessons: " + totalLessons);
    System.out.println("Async updateCourseProgress - Completed lessons: " + completedLessons);

    CourseCompletion courseCompletion = courseCompletionRepo
            .findByUserAndCourse(user, course)
            .orElseGet(() -> new CourseCompletion(user, course, null));

    double progressPercent = totalLessons > 0 ? (completedLessons * 100.0) / totalLessons : 0.0;

    boolean updatedProgress = false;

    if (progressPercent > courseCompletion.getProgressPercent()) {
        courseCompletion.setProgressPercent(progressPercent);
        courseCompletion.setLessonsCompleted((int) completedLessons);
        courseCompletion.setLastAccessed(LocalDateTime.now());
        updatedProgress = true;
    }

    boolean isAllLessonsCompleted = completedLessons == totalLessons;

    if (isAllLessonsCompleted && !courseCompletion.isCompleted()) {
        courseCompletion.setCompleted(true);
        courseCompletion.setCompletedOn(LocalDateTime.now());
        updatedProgress = true;
    }

    if (updatedProgress) {
        courseCompletionRepo.save(courseCompletion);
    }
}

	    public List<Long> getCompletedLessonIds(Long userId, Long courseId) {
	        return lessonProgressRepo.findCompletedLessonIdsByUserAndCourse(userId, courseId);
	    }
}
