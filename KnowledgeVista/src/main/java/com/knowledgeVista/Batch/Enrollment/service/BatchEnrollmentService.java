package com.knowledgeVista.Batch.Enrollment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.Batch.Enrollment.BatchEnrollment;
import com.knowledgeVista.Batch.Enrollment.BatchRoleMapping;
import com.knowledgeVista.Batch.Enrollment.repo.BatchEnrollmentRepo;
import com.knowledgeVista.Batch.Enrollment.repo.BatchRoleMappingRepo;
import com.knowledgeVista.Batch.Repo.BatchRepository;
import com.knowledgeVista.Batch.service.EnrollNotificationService;
import com.knowledgeVista.Course.CourseDetail;
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


	
}
