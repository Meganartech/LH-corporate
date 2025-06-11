package com.knowledgeVista.Course.Controller;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.knowledgeVista.Batch.Enrollment.repo.BatchEnrollmentRepo;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Course.Repository.CourseDetailRepository;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

@RestController
@CrossOrigin
public class CheckAccess {
	@Autowired
	private CourseDetailRepository coursedetailrepository;
	@Autowired
	private MuserRepositories muserRepository;
	@Autowired
	private BatchEnrollmentRepo batchEnrollRepo;
	@Autowired
	private JwtUtil jwtUtil;


  	 private static final Logger logger = LoggerFactory.getLogger(CheckAccess.class);

	 public ResponseEntity<?> checkAccess( Map<String, Long> requestData,  String token) {
	     try {
	       

	         String email = jwtUtil.getEmailFromToken(token);

	         Long courseId = requestData.get("courseId");
	         Optional<CourseDetail> courseOptional = coursedetailrepository.findById(courseId);
	         Optional<Muser> optionalUser = muserRepository.findByEmail(email);
	         if(!courseOptional.isPresent()) {
	        	 return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Course Not Found");
	         }
	         if(!optionalUser.isPresent()) {
	        	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
	         }
	        	 CourseDetail course = courseOptional.get();
	        	 Muser user = optionalUser.get();
	        	 
	        	 String role=user.getRole().getRoleName();
	        	 
	        	   String courseUrl = course.getCourseUrl();
	        	
	         if ("ADMIN".equals(role)) {
	                 return ResponseEntity.ok().body(courseUrl);
	             
	         } else {
	        	 if(course.isApprovalNeeded()) {
	        	 if(batchEnrollRepo.existsActiveCourseForUser(user.getUserId(), courseId)) {
	        		  return ResponseEntity.ok().body(courseUrl);
	        	 }else{
	        		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you cannot Access This Course Approval Needed");
	        		 //need to send Approval Request
	        	 }
	        	 }else {
	        		  return ResponseEntity.ok().body(courseUrl);
	        	 }
	         }
	         
	       

	     } catch (Exception e) {
	         // Handle any exceptions
//	    	 e.printStackTrace();    
	    	 logger.error("", e);;
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	     }
	 }
}