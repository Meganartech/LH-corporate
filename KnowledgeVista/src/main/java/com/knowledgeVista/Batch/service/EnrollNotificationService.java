package com.knowledgeVista.Batch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Email.EmailService;
import com.knowledgeVista.Notification.Service.NotificationService;
import com.knowledgeVista.User.Muser;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class EnrollNotificationService {
	
	 @Autowired
		private NotificationService notiservice;
	 @Autowired
	 private EmailService emailService;
	 
	 private static final Logger logger = LoggerFactory.getLogger(EnrollNotificationService.class);

	
		public void sendEnrollmentMail(HttpServletRequest request,List<CourseDetail> courses,Batch batch,Muser student ,String adding) {
		try {
			 List<Long> userList = new ArrayList<>();
		        userList.add(student.getUserId());

		        // Notification for the user (Trainer or Student)
		        String heading = "New Batch Assigned!";
		        String link = "/mycourses"; // Trainer and Student have different links
		        String notiDescription = "A batch " + batch.getBatchTitle() + " was assigned to you";

		        Long notifyId = notiservice.createNotification("CourseAssigned", student.getUsername(), notiDescription, adding, heading, link, batch.getBatchImage());
		        if (notifyId != null) {
		            notiservice.SpecificCreateNotification(notifyId, userList);
		        }
			List<String> bcc = null;
			List<String> cc = null;
			String institutionname = student.getInstitutionName();
		       String domain = request.getHeader("origin"); // Extracts the domain dynamically

		         // Fallback if "Origin" header is not present (e.g., direct backend requests)
		         if (domain == null || domain.isEmpty()) {
		             domain = request.getScheme() + "://" + request.getServerName();
		             if (request.getServerPort() != 80 && request.getServerPort() != 443) {
		                 domain += ":" + request.getServerPort();
		             }
		         }

		         // Construct the Sign-in Link
		         String signInLink = domain + "/login";
		         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy HH:mm"); // Example: 5-Jan-2025
		         String formattedStartDate = LocalDate.now().format(formatter);
		         String formattedEndDate = LocalDateTime.now().plusHours(batch.getDurationInHours()).format(formatter);
		         StringBuilder body = new StringBuilder();

		       
		         body.append("<html>")
		             .append("<body>")
		             .append("<h2>Welcome to LearnHub - Your Learning Journey Begins!</h2>")
		             .append("<p>Dear ").append(student.getUsername()).append(",</p>")
		             .append("<p>Congratulations! You have been successfully enrolled in <strong>Batch: ")
		             .append(batch.getBatchTitle()).append(" (").append(formattedStartDate).append(" to ").append(formattedEndDate).append(")</strong> at LearnHub.</p>")
		             .append("<p>In this batch, you will be learning the below courses:</p>")
		             .append("<ul>").append(generateCourseList(batch.getCourses())).append("</ul>")
		             .append("<p>To get started:</p>")
		             .append("<ul>")
		             .append("<li>Log in to your LearnHub account.</li>")
		             .append("<li>Access your enrolled courses in My Courses Tab.</li>")
		             .append("</ul>")
		             .append("<p>If you need any assistance, our support team is here to help.</p>")
		             .append("<p>Click the link below to sign in:</p>")
		             .append("<p><a href='").append(signInLink).append("' style='font-size:16px; color:blue;'>Sign In</a></p>")
		             .append("<p>Best Regards,<br>LearnHub Team</p>")
		             .append("</body>")
		             .append("</html>");

		         
			if (institutionname != null && !institutionname.isEmpty()) {
			    try {
			        List<String> emailList = new ArrayList<>();
			        emailList.add(student.getEmail());
			        emailService.sendHtmlEmailAsync(
			            institutionname, 
			            emailList,
			            cc, 
			            bcc, 
			            "Welcome to LearnHub - Batch Enrollment Successful!", 
			            body.toString()
			        );
			    } catch (Exception e) {
			        logger.error("Error sending mail: " + e.getMessage());
			    }
			   
			}
		}catch (Exception e) {
			   logger.error("Error sending mail: " + e.getMessage());
		}

			// Helper method to format courses as an HTML list
			

		}
		private String generateCourseList(List<CourseDetail> courses) {
		    StringBuilder courseList = new StringBuilder();
		    for (CourseDetail course : courses) {
		        courseList.append("<li>").append(course.getCourseName()).append("</li>");
		    }
		    return courseList.toString();
		}
		
////============================Assign Batch To Trainer====================================
				
		 

}
