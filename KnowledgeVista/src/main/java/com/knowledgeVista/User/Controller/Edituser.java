package com.knowledgeVista.User.Controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

@RestController
@RequestMapping("/Edit")
@CrossOrigin
public class Edituser {
	@Autowired
	private MuserRepositories muserrepositories;
	 @Autowired
	 private JwtUtil jwtUtil;
	 
	 private static final Logger logger = LoggerFactory.getLogger(Edituser.class);

	
	 public ResponseEntity<?> updateStudent( String originalEmail, String username, String newEmail, LocalDate dob,
	     String phone, String skills,MultipartFile profile, Boolean isActive,String countryCode, String token
	 ) {
	     try {
	         // Validate the token
	         if (!jwtUtil.validateToken(token)) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized access\"}");
	         }

	         // Get the role from the token
	         String role = jwtUtil.getRoleFromToken(token);

	         // Only ADMIN and TRAINER roles are allowed
	         if (!role.equals("ADMIN") && !role.equals("TRAINER")) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized access\"}");
	         }

	         // Fetch the student by the original email
	         Optional<Muser> opStudent = muserrepositories.findByEmail(originalEmail);
	         if (!opStudent.isPresent()) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Student not found\"}");
	         }

	         // Get the student object
	         Muser student = opStudent.get();

	         // If the role is not USER, return an error
	         if (!"USER".equals(student.getRole().getRoleName())) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Student not found\"}");
	         }

	         // Update student data
	         student.setUsername(username);

	         // Check if the email has changed
	         if (!originalEmail.equals(newEmail)) {
	             // Check if the new email already exists
	             Optional<Muser> existingUser = muserrepositories.findByEmail(newEmail);
	             if (existingUser.isPresent()) {
	                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"EMAIL ALREADY EXISTS\"}");
	             } else {
	                 // Update the email
	                 student.setEmail(newEmail);
	             }
	         }

	        
	        	    student.setDob(dob);  // dob should be of type LocalDate
	        
	         student.setPhone(phone);
	         student.setSkills(skills);
	         student.setIsActive(isActive);
	         student.setCountryCode(countryCode);

	         // Compress and set profile image
	         if (profile != null && !profile.isEmpty()) {
	             student.setProfile(profile.getBytes());
	         }

	         // Save the updated student
	         muserrepositories.save(student);

	         // Return successful response
	         return ResponseEntity.ok("{\"message\": \"Student updated successfully\"}");

	     } catch (Exception e) {
	         // Log any exceptions for debugging purposes
	         e.printStackTrace(); // Use a logging framework like Log4j in production
	         logger.error("", e);
	         // Return an internal server error response
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An error occurred while updating the student\"}");
	     }
	 }
	 
	 public ResponseEntity<?> updateDynamicRole(
		        String originalEmail,
		        String username,
		        String newEmail,
		        LocalDate dob,
		        String phone,
		        String skills,
		        MultipartFile profile,
		        Boolean isActive,
		        String countryCode,
		        String token,
		        String expectedRole) {

		    try {
		        /* ─────── 1. AUTHZ  ─────── */
		        if (!jwtUtil.validateToken(token)) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                                 .body("{\"message\":\"Unauthorized access\"}");
		        }

		        String callerRole = jwtUtil.getRoleFromToken(token);
		        if (!"ADMIN".equals(callerRole)) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		                                 .body("{\"message\":\"Unauthorized access\"}");
		        }

		        /* ─────── 2. FETCH USER  ─────── */
		        Optional<Muser> opUser = muserrepositories.findByEmail(originalEmail);
		        if (opUser.isEmpty())
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("{\"message\":\"User not found\"}");

		        Muser user = opUser.get();

		        /* ensure we are editing the intended role */
		        if (!expectedRole.equalsIgnoreCase(user.getRole().getRoleName())) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
		                                 .body("{\"message\":\"" + expectedRole + " not found\"}");
		        }

		        /* ─────── 3. EMAIL CONFLICT CHECK  ─────── */
		        if (!originalEmail.equalsIgnoreCase(newEmail)) {
		            if (muserrepositories.findByEmail(newEmail).isPresent()) {
		                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                                     .body("{\"message\":\"EMAIL ALREADY EXISTS\"}");
		            }
		            user.setEmail(newEmail);
		        }

		        /* ─────── 4. UPDATE FIELDS  ─────── */
		        user.setUsername(username);
		        user.setDob(dob);
		        user.setPhone(phone);
		        user.setSkills(skills);
		        user.setIsActive(isActive);
		        user.setCountryCode(countryCode);

		        if (profile != null && !profile.isEmpty()) {
		            user.setProfile(profile.getBytes());   // or compress before saving
		        }

		        /* ─────── 5. SAVE & RESPOND  ─────── */
		        muserrepositories.save(user);
		        return ResponseEntity.ok("{\"message\":\"" + expectedRole +
		                                 " updated successfully\"}");

		    } catch (Exception ex) {
		        logger.error("Error updating " + expectedRole, ex);
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body("{\"message\":\"An error occurred while updating the "
		                                   + expectedRole + "\"}");
		    }
		}
	 
	 
	 public ResponseEntity<?> updateTrainer( String originalEmail, String username,
			 String newEmail, LocalDate dob,String phone,String skills,
	      MultipartFile profile, Boolean isActive,String countryCode, String token
	 ) {
	     try {
	         // Validate the token
	         if (!jwtUtil.validateToken(token)) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized access\"}");
	         }

	         // Get the role from the token
	         String role = jwtUtil.getRoleFromToken(token);

	         // Only ADMIN and TRAINER roles are allowed
	         if (!role.equals("ADMIN")) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized access\"}");
	         }

	         // Fetch the student by the original email
	         Optional<Muser> opStudent = muserrepositories.findByEmail(originalEmail);
	         if (!opStudent.isPresent()) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Trainer not found\"}");
	         }

	         // Get the student object
	         Muser student = opStudent.get();

	         // If the role is not USER, return an error
	         if (!"TRAINER".equals(student.getRole().getRoleName())) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"Trainer not found\"}");
	         }

	         // Update student data
	         student.setUsername(username);

	         // Check if the email has changed
	         if (!originalEmail.equals(newEmail)) {
	             // Check if the new email already exists
	             Optional<Muser> existingUser = muserrepositories.findByEmail(newEmail);
	             if (existingUser.isPresent()) {
	                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"EMAIL ALREADY EXISTS\"}");
	             } else {
	                 // Update the email
	                 student.setEmail(newEmail);
	             }
	         }

	         // Update other student properties
	         student.setDob(dob);
	         student.setPhone(phone);
	         student.setSkills(skills);
	         student.setIsActive(isActive);
	         student.setCountryCode(countryCode);

	         // Compress and set profile image
	         if (profile != null && !profile.isEmpty()) {
	             student.setProfile(profile.getBytes());
	         }

	         // Save the updated student
	         muserrepositories.save(student);

	         // Return successful response
	         return ResponseEntity.ok("{\"message\": \"Trainer updated successfully\"}");

	     } catch (Exception e) {
	         // Log any exceptions for debugging purposes
	         e.printStackTrace(); // Use a logging framework like Log4j in production
	         logger.error("", e);
	         // Return an internal server error response
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An error occurred while updating the Trainer\"}");
	     }
	 }

	 
	 
	 public ResponseEntity<?> EditProfile( String username, String newEmail, LocalDate dob, String phone,
	     String skills, MultipartFile profile, Boolean isActive,String countryCode ,String token
	 ) {
	     try {
	         // Validate the token
	         if (!jwtUtil.validateToken(token)) {
	             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Unauthorized access\"}");
	         }

	         jwtUtil.getRoleFromToken(token);
	         String originalEmail=jwtUtil.getEmailFromToken(token);
	         Optional<Muser> opStudent = muserrepositories.findByEmail(originalEmail);
	         if (!opStudent.isPresent()) {
	             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"user not found\"}");
	         }
	         Muser student = opStudent.get();

	        
	         student.setUsername(username);

	         // Check if the email has changed
	         if (!originalEmail.equals(newEmail)) {
	             // Check if the new email already exists
	             Optional<Muser> existingUser = muserrepositories.findByEmail(newEmail);
	             if (existingUser.isPresent()) {
	                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"EMAIL ALREADY EXISTS\"}");
	             } else {
	                 // Update the email
	                 student.setEmail(newEmail);
	             }
	         }

	         if(dob !=null) {
	         student.setDob(dob);
	         }
	         student.setPhone(phone);
	         student.setSkills(skills);
	         student.setIsActive(isActive);
	         student.setCountryCode(countryCode);

	         // Compress and set profile image
	         if (profile != null && !profile.isEmpty()) {
	             student.setProfile(profile.getBytes());
	         }

	         // Save the updated student
	         muserrepositories.save(student);

	         // Return successful response
	         return ResponseEntity.ok("{\"message\": \" your Profile updated successfully\"}");

	     } catch (Exception e) {
	         // Log any exceptions for debugging purposes
	         e.printStackTrace(); // Use a logging framework like Log4j in production
	         logger.error("", e);
	         // Return an internal server error response
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An error occurred while updating your profile please try again later\"}");
	     }
	 }

	 
	 public ResponseEntity<?> NameandProfile( String token) {
		 
		 String email= jwtUtil.getEmailFromToken(token);
		 
		Optional<Muser> opuser= muserrepositories.findByEmail(email);
		if(opuser.isPresent()) {
			Muser user=opuser.get();
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("name", user.getUsername());
           
            responseBody.put("profileImage",user.getProfile());
          

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responseBody);
			
		}
		return ResponseEntity.notFound().build();
	 }
//need to work in inactive institution
	
}