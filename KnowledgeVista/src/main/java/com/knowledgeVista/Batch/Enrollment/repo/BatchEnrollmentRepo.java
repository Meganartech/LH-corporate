package com.knowledgeVista.Batch.Enrollment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.knowledgeVista.Batch.Enrollment.BatchEnrollment;
import com.knowledgeVista.Course.CourseDetailDto;


@Repository
public interface BatchEnrollmentRepo extends JpaRepository<BatchEnrollment, Long>{
	//For MyCourses........................
	@Query("""
		    SELECT DISTINCT new com.knowledgeVista.Course.CourseDetailDto(
		        c.id, c.courseName, c.courseUrl, c.courseDescription,
		        c.courseCategory, c.amount, c.courseImage,
		        c.Duration, c.institutionName
		    )
		    FROM BatchEnrollment be
		    JOIN be.batch b
		    JOIN b.courses c
		    WHERE be.user.email = :email
		    AND be.expiryDate > CURRENT_TIMESTAMP
		""")
		List<CourseDetailDto> findActiveCoursesByUserEmail(@Param("email") String email);
	
	//for finding the user has Access To course
	@Query("""
		    SELECT COUNT(be) > 0
		    FROM BatchEnrollment be
		    JOIN be.batch b
		    JOIN b.courses c
		    WHERE be.user.id = :userId
		      AND c.id = :courseId
		      AND be.expiryDate > CURRENT_TIMESTAMP
		""")
		boolean existsActiveCourseForUser(@Param("userId") Long userId, @Param("courseId") Long courseId);

	


}
