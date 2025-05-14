package com.knowledgeVista.Batch.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.knowledgeVista.Batch.Repo.BatchRepository;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

@Service
public class BatchService2 {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private BatchRepository batchrepo;
	@Autowired
	private MuserRepositories muserRepo;
	private static final Logger logger = LoggerFactory.getLogger(BatchService2.class);

	public ResponseEntity<?> getEnrolledBatches(String token, Long userId, int page, int size) {
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

			// Define pagination parameters
			Pageable pageable = PageRequest.of(page, size);
			int pageSize = pageable.getPageSize(); // Number of records per page
			int offset = pageable.getPageNumber() * pageSize; // Offset calculation

			// Fetch paginated data
			List<Map<String, Object>> batches = batchrepo.findBatchesByUserIdWithPagination(userId, pageSize, offset);

			// Fetch total count for pagination
			long totalRecords = batchrepo.countBatchesByUserId(userId);

			// Create Page object
			Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);

			return ResponseEntity.ok(pagedResponse);

		} catch (Exception e) {
			logger.error("Error Getting Assigned Batches: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getOtherBatches(String token, Long userId, int page, int size) {
		try {
			// Validate JWT token
			if (!jwtUtil.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}

			// Check user role
			String role = jwtUtil.getRoleFromToken(token);
			if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Cannot Access This Page");
			}

			// Define pagination parameters
			Pageable pageable = PageRequest.of(page, size);
			int pageSize = pageable.getPageSize(); // Number of records per page
			int offset = pageable.getPageNumber() * pageSize; // Offset calculation

			// Fetch paginated data
			List<Map<String, Object>> batches = batchrepo.findInstitutionBatchesWithPagination(userId, pageSize,
					offset);

			// Fetch total count for pagination
			long totalRecords = batchrepo.countBatchesNotEnrolledByUserId(userId);

			// Create Page object
			Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);

			return ResponseEntity.ok(pagedResponse);

		} catch (Exception e) {
			logger.error("Error Getting Assigned Batches: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

//======================================For Trainer===============================================
	public ResponseEntity<?> getbatchesForTrainer(String token, Long userId, int page, int size) {
		try {
			// Validate JWT token
			if (!jwtUtil.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}

			// Check user role
			String role = jwtUtil.getRoleFromToken(token);
			if (!"ADMIN".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Admin Can Access This Page");
			}

			// Define pagination parameters
			Pageable pageable = PageRequest.of(page, size);
			int pageSize = pageable.getPageSize(); // Number of records per page
			int offset = pageable.getPageNumber() * pageSize; // Offset calculation

			// Fetch paginated data
			List<Map<String, Object>> batches = batchrepo.findAssignedBatchesForTrainerIdWithPagination(userId,
					pageSize, offset);

			// Fetch total count for pagination
			long totalRecords = batchrepo.countAssignedBatchesForTrainerId(userId);

			// Create Page object
			Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);

			return ResponseEntity.ok(pagedResponse);

		} catch (Exception e) {
			logger.error("Error Getting Assigned Batches: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<?> getOtherBatchesForTrainer(String token, Long userId, int page, int size) {
		try {
			// Validate JWT token
			if (!jwtUtil.validateToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}

			// Check user role
			String role = jwtUtil.getRoleFromToken(token);
			if (!"ADMIN".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Cannot Access This Page");
			}

			// Define pagination parameters
			Pageable pageable = PageRequest.of(page, size);
			int pageSize = pageable.getPageSize(); // Number of records per page
			int offset = pageable.getPageNumber() * pageSize; // Offset calculation

			// Fetch paginated data
			List<Map<String, Object>> batches = batchrepo.findBatchesNotAssignedForTrainerIdWithPagination(userId,
					pageSize, offset);

			// Fetch total count for pagination
			long totalRecords = batchrepo.countBatchesNotAssignedForTrainerId(userId);

			// Create Page object
			Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);

			return ResponseEntity.ok(pagedResponse);

		} catch (Exception e) {
			logger.error("Error Getting Assigned Batches: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	public ResponseEntity<?> getOtherBatchesforRole(String token, Long roleId, int page, int size) {
	    try {
	        // Validate JWT token
	        if (!jwtUtil.validateToken(token)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        // Get user
	        String email = jwtUtil.getUsernameFromToken(token);
	        Optional<Muser> opUser = muserRepo.findByEmail(email);

	        if (opUser.isPresent()) {
	            Muser user = opUser.get();
	            String role = user.getRole().getRoleName();

	            if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Cannot Access This Page");
	            }

	            String institutionName = user.getInstitutionName();

	            // Define pagination parameters
	            Pageable pageable = PageRequest.of(page, size);
	            int pageSize = pageable.getPageSize();
	            int offset = pageable.getPageNumber() * pageSize;

	            // Fetch paginated data
	            List<Map<String, Object>> batches = batchrepo.findBatchesNotMappedToRoleInInstitution(
	                roleId, institutionName, pageSize, offset
	            );

	            // Fetch total count
	            long totalRecords = batchrepo.countBatchesNotMappedToRoleInInstitution(roleId, institutionName);

	            // Return paginated result
	            Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);
	            return ResponseEntity.ok(pagedResponse);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	    } catch (Exception e) {
	        logger.error("Error Getting Assigned Batches: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}
	
	
	
	
	public ResponseEntity<?> getBatchesforRole(String token, Long roleId, int page, int size) {
	    try {
	        // Validate JWT token
	        if (!jwtUtil.validateToken(token)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        // Get user
	        String email = jwtUtil.getUsernameFromToken(token);
	        Optional<Muser> opUser = muserRepo.findByEmail(email);

	        if (opUser.isPresent()) {
	            Muser user = opUser.get();
	            String role = user.getRole().getRoleName();

	            if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Cannot Access This Page");
	            }

	            String institutionName = user.getInstitutionName();

	            // Define pagination parameters
	            Pageable pageable = PageRequest.of(page, size);
	            int pageSize = pageable.getPageSize();
	            int offset = pageable.getPageNumber() * pageSize;

	            // Fetch paginated data
	            List<Map<String, Object>> batches = batchrepo.findBatchesMappedToRoleInInstitution(
	                roleId, institutionName, pageSize, offset
	            );

	            // Fetch total count
	            long totalRecords = batchrepo.countBatchesMappedToRoleInInstitution(roleId, institutionName);

	            // Return paginated result
	            Page<Map<String, Object>> pagedResponse = new PageImpl<>(batches, pageable, totalRecords);
	            return ResponseEntity.ok(pagedResponse);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	    } catch (Exception e) {
	        logger.error("Error Getting Assigned Batches: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	    }
	}


}
