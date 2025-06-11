package com.knowledgeVista.Batch.Enrollment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.Batch.Enrollment.AccessRequest;
import com.knowledgeVista.Batch.Enrollment.AccessRequest.RequestStatus;
import com.knowledgeVista.Batch.Enrollment.BatchEnrollment;
import com.knowledgeVista.Batch.Enrollment.BatchRoleMapping;
import com.knowledgeVista.Batch.Enrollment.repo.AccessRequestRepo;
import com.knowledgeVista.Batch.Enrollment.repo.BatchEnrollmentRepo;
import com.knowledgeVista.Batch.Enrollment.repo.BatchRoleMappingRepo;
import com.knowledgeVista.Batch.Repo.BatchRepository;
import com.knowledgeVista.Batch.service.EnrollNotificationService;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Notification.Service.NotificationService;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.MuserRoles;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.Repository.MuserRoleRepository;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;


@Service
public class BatchEnrollmentService {

	@Autowired
	private BatchRepository batchrepo;
	@Autowired
	private BatchRoleMappingRepo batchRoleMappingRepo;
	@Autowired
	private MuserRepositories muserRepo;
	@Autowired
	private BatchEnrollmentRepo batchEnrollRepo;
	@Autowired
	private MuserRoleRepository roleRepo;
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired 
	private AccessRequestRepo AccessRequestRepo;
	
	@Autowired
	private NotificationService notiservice;
	
	@Autowired
	private EnrollNotificationService enrollNotiService;

	private static final Logger logger = LoggerFactory.getLogger(BatchEnrollmentService.class);
	
	public ResponseEntity<?> AssignDefaultBatchesPub(HttpServletRequest request, Muser user) {
		//need to add RoleSpecific Check in Future After Featre Enabling
	return AssignDefaultBatches(request,user,"System");
	}
	private ResponseEntity<?> AssignDefaultBatches(HttpServletRequest request, Muser user ,String addinguserName) {
	    try {
	        List<BatchRoleMapping> mappings = batchRoleMappingRepo.findbyRoleId(user.getRole().getRoleId());

	        List<BatchEnrollment> enrollments = mappings.stream()
	            .map(map -> new BatchEnrollment(user, map.getBatch(), map.getBatch().getDurationInHours()))
	            .toList();

	        batchEnrollRepo.saveAll(enrollments);

	        // Send mail for each batch enrolled
	        for (BatchRoleMapping map : mappings) {
	            List<CourseDetail> courses = map.getBatch().getCourses();// assuming you have such a method
	            enrollNotiService.sendEnrollmentMail(request, courses, map.getBatch(), user, addinguserName);
	        }

	        return ResponseEntity.ok("saved");
	    } catch (Exception e) {
	        logger.error("Error occurred in Enroll Batch To User: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	private ResponseEntity<?> EnrollBatchTOUser(HttpServletRequest request, List<Muser> users, Batch batch,String addinguserName) {
	    try {
	        List<BatchEnrollment> enrollments = users.stream()
	            .map(user -> new BatchEnrollment(user, batch, batch.getDurationInHours()))
	            .toList();

	        batchEnrollRepo.saveAll(enrollments);

	        List<CourseDetail> courses = batch.getCourses();
	        for (Muser user : users) {
	            enrollNotiService.sendEnrollmentMail(request, courses, batch, user,addinguserName);
	        }

	        return ResponseEntity.ok("Batch assigned successfully");
	    } catch (Exception e) {
	        logger.error("Error occurred in Enroll Batch To User: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	//Assign batch To Role
	public ResponseEntity<?> AssignBatchTORole(HttpServletRequest request,String token, Long roleId,Long batchId) {
		try {
			
			String role = jwtUtil.getRoleFromToken(token);
			String ausername=jwtUtil.getUsernameFromToken(token);
			if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin Can Access This Page");
			}
			Optional<Batch> opbatch=batchrepo.findById(batchId);
			if(opbatch.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("batch Not Found");
			}
			Batch batch=opbatch.get();
			Optional<MuserRoles> opmuserRole = roleRepo.findById(roleId);
			if(opmuserRole.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role Not Found");
			}
			MuserRoles muserRole=opmuserRole.get();
			BatchRoleMapping roleMapping=new BatchRoleMapping(muserRole,batch);
			batchRoleMappingRepo.save(roleMapping);
			List<Muser> users=muserRepo.findAllByroleid(roleId);
			return EnrollBatchTOUser(request,users, batch,ausername);
		}catch (Exception e) {
			logger.error("Error occured in Enroll Batch To User :" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
		
		public ResponseEntity<?> AssignBatchTOUser(HttpServletRequest request ,String token, Long userId,Long batchId) {
			try {
				
				String role = jwtUtil.getRoleFromToken(token);
				String ausername=jwtUtil.getUsernameFromToken(token);
				if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin Can Access This Page");
				}
				Optional<Batch> opbatch=batchrepo.findById(batchId);
				if(opbatch.isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("batch Not Found");
				}
				Batch batch=opbatch.get();
				Optional<Muser> opmuser = muserRepo.findById(userId);
				if(opmuser.isEmpty()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user Not Found");
				}
				Muser muser=opmuser.get();
				List<Muser>users=new ArrayList<Muser>();
				users.add(muser);
				return EnrollBatchTOUser(request,users, batch,ausername);
			}catch (Exception e) {
				logger.error("Error occured in Enroll Batch To User :" + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}

		 public ResponseEntity<?> createAccessRequest( Long batchId,String token) {
		        // Check if user exists
			 Long userId=jwtUtil.getUserIdFromToken(token);
		        Muser user = muserRepo.findById(userId).orElse(null);
		        if (user == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		        }

		        // Check if batch exists
		        Batch batch = batchrepo.findById(batchId).orElse(null);
		        if (batch == null) {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batch not found");
		        }

		        Optional<AccessRequest> existingRequest = AccessRequestRepo.findByUserIdAndBatchId(userId, batchId);

		        if (existingRequest.isPresent()) {
		            AccessRequest request = existingRequest.get();

		            if (request.getStatus() == RequestStatus.PENDING) {
		                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                                     .body("Access request already submitted and pending.");
		            } else if (request.getStatus() == RequestStatus.REJECTED) {
		                AccessRequestRepo.delete(request); // allow re-request
		            } else {
		                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                                     .body("Access already granted for this batch.");
		            }
		        }

		        // Create new access request
		        AccessRequest newRequest = new AccessRequest();
		        newRequest.setUser(user);
		        newRequest.setBatch(batch);
		        newRequest.setRequestTime(LocalDateTime.now());
		        newRequest.setStatus(RequestStatus.PENDING);

		        AccessRequestRepo.save(newRequest);


		        return ResponseEntity.ok("Access request sent successfully.");
		    }


		 public ResponseEntity<?> getAccessRequestList(String token) {
		       try {
			 String role=jwtUtil.getRoleFromToken(token);
		     if("ADMIN".equals(role)) {
		    	 return ResponseEntity.ok(AccessRequestRepo.findPendingAccessRequestDTOs());
		     }else {
		    	 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		     }
		       }catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		    }

		 public ResponseEntity<?> approveEnrollmentApproval(HttpServletRequest request, String token, List<Long> ids) {
			    try {
			        String role = jwtUtil.getRoleFromToken(token);
			        String username = jwtUtil.getUsernameFromToken(token);
			        if (!"ADMIN".equals(role)) {
			            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			        }

			        List<AccessRequest> requests = AccessRequestRepo.findAllById(ids).stream()
			            .filter(r -> r.getStatus() == RequestStatus.PENDING)
			            .toList();

			        if (requests.isEmpty()) {
			            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			        }

			        for (AccessRequest accessRequest : requests) {
			            accessRequest.setStatus(RequestStatus.APPROVED);
			            EnrollBatchTOUser(request, Collections.singletonList(accessRequest.getUser()), accessRequest.getBatch(), username);
			        }
			        AccessRequestRepo.saveAll(requests);
			        return ResponseEntity.ok("Approved " + requests.size() + " requests");
			    } catch (Exception e) {
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
			    }
			}

			public ResponseEntity<?> rejectEnrollmentApproval(String token, List<Long> ids) {
			    try {
			        String role = jwtUtil.getRoleFromToken(token);
			        String username=jwtUtil.getUsernameFromToken(token);
			        if (!"ADMIN".equals(role)) {
			            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			        }

			        List<AccessRequest> requests = AccessRequestRepo.findAllById(ids).stream()
			            .filter(r -> r.getStatus() == RequestStatus.PENDING)
			            .toList();

			        if (requests.isEmpty()) {
			            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			        }

			        for (AccessRequest accessRequest : requests) {
			            accessRequest.setStatus(RequestStatus.REJECTED);
			            sendRejectNoti(accessRequest.getUser(),accessRequest.getBatch(),username);
			        }
			        AccessRequestRepo.saveAll(requests);
			        return ResponseEntity.ok("Rejected " + requests.size() + " requests");
			    } catch (Exception e) {
			        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
			    }
			}
private void sendRejectNoti(Muser student , Batch batch,String adding) {
	 List<Long> userList = new ArrayList<>();
     userList.add(student.getUserId());

     // Notification for the user (Trainer or Student)
     String heading = "Enroll Request Rejected!";
     String link = "/mycourses"; // Trainer and Student have different links!\
     String notiDescription = "A batch " + batch.getBatchTitle() + " Enrollment Request was Rejected..!";

     Long notifyId = notiservice.createNotification("CourseAssigned", student.getUsername(), notiDescription, adding, heading, link, batch.getBatchImage());
     if (notifyId != null) {
         notiservice.SpecificCreateNotification(notifyId, userList);
     }
}
}
