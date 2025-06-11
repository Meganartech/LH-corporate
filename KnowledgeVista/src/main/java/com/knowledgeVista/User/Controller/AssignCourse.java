package com.knowledgeVista.User.Controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.knowledgeVista.Batch.Enrollment.repo.BatchEnrollmentRepo;
import com.knowledgeVista.Course.CourseDetailDto;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

@Service
public class AssignCourse {
	@Autowired
	private MuserRepositories muserRepository;
	@Autowired
	 private JwtUtil jwtUtil;
	 @Autowired
	 private BatchEnrollmentRepo batchEnrollRepo;
	 
	 private static final Logger logger = LoggerFactory.getLogger(AssignCourse.class);
public ResponseEntity<List<CourseDetailDto>> getCoursesForUser( String token) {
	try {
        String email = jwtUtil.getEmailFromToken(token);
			 List<CourseDetailDto> courses=batchEnrollRepo.findActiveCoursesWithProgressByUserEmail(email);		  
		        return ResponseEntity.ok(courses);   
	}catch (Exception e) {
		logger.error("Error Getting courses  for user "+e.getMessage());
		return ResponseEntity.ok(Collections.emptyList()); 
	}
	    }
	 

public ResponseEntity<List<CourseDetailDto>> getCoursesForTrainer(String token) {
    String role = jwtUtil.getRoleFromToken(token);
    String email = jwtUtil.getEmailFromToken(token);
	if("TRAINER".equals(role)) {
		 List<CourseDetailDto> courses=muserRepository.findAllotedCoursesByEmail(email);
  
        return ResponseEntity.ok(courses);
        }

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    
}

	
}
