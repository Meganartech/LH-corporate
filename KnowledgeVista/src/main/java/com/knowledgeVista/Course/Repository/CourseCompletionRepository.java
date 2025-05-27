package com.knowledgeVista.Course.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.knowledgeVista.Course.CourseCompletion;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.User.Muser;

import java.util.Optional;

public interface CourseCompletionRepository extends JpaRepository<CourseCompletion, Long> {
    Optional<CourseCompletion> findByUserAndCourse(Muser user, CourseDetail course);
}
