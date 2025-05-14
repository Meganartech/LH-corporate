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
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.MuserRoles;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.Repository.MuserRoleRepository;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;


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

	private static final Logger logger = LoggerFactory.getLogger(BatchEnrollmentService.class);
	
	public ResponseEntity<?> AssignDefaultBatchesPub(Muser user) {
		//need to add RoleSpecific Check in Future After Featre Enabling
	return AssignDefaultBatches(user);
	}
	private ResponseEntity<?> AssignDefaultBatches(Muser user) {
	    try {
	        List<BatchRoleMapping> mappings = batchRoleMappingRepo.findbyRoleId(user.getRole().getRoleId());

	        // Create all enrollments
	        List<BatchEnrollment> enrollments = mappings.stream()
	            .map(map -> new BatchEnrollment(user, map.getBatch(), map.getBatch().getDurationInHours()))
	            .toList(); // or use .collect(Collectors.toList()) if using Java < 16

	        // Save all at once
	        batchEnrollRepo.saveAll(enrollments);

	        return ResponseEntity.ok("saved");
	    } catch (Exception e) {
	        logger.error("Error occurred in Enroll Batch To User: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	private ResponseEntity<?> EnrollBatchTOUser(List<Muser> users, Batch batch) {
	    try {
	        // Prepare all BatchEnrollment entities
	        List<BatchEnrollment> enrollments = users.stream()
	            .map(user -> new BatchEnrollment(user, batch, batch.getDurationInHours()))
	            .toList(); // Use collect(Collectors.toList()) if you're on Java < 16

	        // Save all at once
	        batchEnrollRepo.saveAll(enrollments);

	        return ResponseEntity.ok("Batch assigned successfully");
	    } catch (Exception e) {
	        logger.error("Error occurred in Enroll Batch To User: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	//Assign batch To Role
	public ResponseEntity<?> AssignBatchTORole(String token, Long roleId,Long batchId) {
		try {
			// Validate JWT token
			if (!jwtUtil.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}

			// Check user role
			String role = jwtUtil.getRoleFromToken(token);
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
			return EnrollBatchTOUser(users, batch);
		}catch (Exception e) {
			logger.error("Error occured in Enroll Batch To User :" + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

}
	
}
