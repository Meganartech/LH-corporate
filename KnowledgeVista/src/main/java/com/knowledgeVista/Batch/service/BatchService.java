package com.knowledgeVista.Batch.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.Batch.BatchDto;
import com.knowledgeVista.Batch.BatchImageDTO;
import com.knowledgeVista.Batch.Repo.BatchRepository;
import com.knowledgeVista.Course.CourseDetail;
import com.knowledgeVista.Course.CourseDetailDto;
import com.knowledgeVista.Course.CourseDetailDto.courseIdNameImg;
import com.knowledgeVista.Course.Repository.CourseDetailRepository;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.MuserDto;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

@Service
public class BatchService {

	@Autowired
	private CourseDetailRepository courseDetailRepository;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private MuserRepositories muserRepo;
	@Autowired
	private BatchRepository batchrepo;
	private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

	public List<CourseDetailDto> searchCourses(String courseName, String token) {
		// Extract email from the JWT token
		String email = jwtUtil.getEmailFromToken(token);

		// Retrieve the institution name associated with the email
		String institutionName = muserRepo.findinstitutionByEmail(email);

		// Query the course details repository
		List<CourseDetailDto> results = courseDetailRepository.searchCourses(courseName,
				institutionName);
		return results;

	}

	public List<Map<String, Object>> searchbatch(String batchTitle, String token) {
		// Extract email from the JWT token
		String email = jwtUtil.getEmailFromToken(token);

		// Retrieve the institution name associated with the email
		String institutionName = muserRepo.findinstitutionByEmail(email);

		// Query the course details repository
		List<Object[]> results = batchrepo.searchBatchByBatchNameAndInstitution(batchTitle, institutionName);

		// Convert List<Object[]> to List<Map<String, Object>>
		return results.stream().map(row -> Map.of("id", row[0], // Map courseId to the first element of the row
				"batchId", row[1], // Map courseName to the second element of the row
				"batchTitle", row[2])).collect(Collectors.toList());
	}

	public List<Map<String, Object>> searchTrainers(String courseName, String token) {
		// Extract email from the JWT token
		String email = jwtUtil.getEmailFromToken(token);

		// Retrieve the institution name associated with the email
		String institutionName = muserRepo.findinstitutionByEmail(email);

		// Query the course details repository
		List<Object[]> results = muserRepo.searchTrainerIddAndTrainerNameByInstitution(courseName, institutionName);

		// Convert List<Object[]> to List<Map<String, Object>>
		return results.stream().map(row -> Map.of("userId", row[0], // Map courseId to the first element of the row
				"username", row[1] // Map courseName to the second element of the row
		)).collect(Collectors.toList());
	}
	// ==================================save
	// Batch======================================

	public ResponseEntity<?> saveBatch(String batchTitle,  Long durationInHours,
			 String coursesJson, MultipartFile batchImage, String token) {
		try {


			String role = jwtUtil.getRoleFromToken(token);
			String addingEmail = jwtUtil.getEmailFromToken(token);

			Optional<Muser> opaddingMuser = muserRepo.findByEmail(addingEmail);
			if (opaddingMuser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
			}

			Muser addingMuser = opaddingMuser.get();

// Only ADMIN and TRAINER can add a batch
			if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Student Cannot add Batch");
			}

// Initialize batch
			Batch batch = new Batch();
			batch.setBatchTitle(batchTitle);
			batch.setDurationInHours(durationInHours);
			batch.setInstitutionName(addingMuser.getInstitutionName());
		

// Parse JSON data
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>> courseList = objectMapper.readValue(coursesJson, List.class);

// Fetch and set courses
			List<CourseDetail> courseDetails = new ArrayList<>();
			for (Map<String, Object> courseMap : courseList) {
				Long courseId = ((Number) courseMap.get("courseId")).longValue();
				courseDetailRepository.findById(courseId).ifPresentOrElse(courseDetails::add,
						() -> logger.warn("Course with ID {} not found. Skipping...", courseId));
			}
			batch.setCourses(courseDetails);

// Save batch image
			if (batchImage != null && !batchImage.isEmpty()) {
				batch.setBatchImage(batchImage.getBytes());
			}

// Save batch to database
			batch = batchrepo.save(batch);

// Prepare response
			Map<String, Object> response = new HashMap<>();
			response.put("batchName", batch.getBatchTitle());
			response.put("batchId", batch.getId());

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			logger.error("Exception occurred in SaveBatch", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// =================================Save Batch for
	// CourseCreation===========================
	public ResponseEntity<?> SaveBatchforCourseCreation(String batchTitle, Long durationInHours,
			String token) {
		try {
			

			String role = jwtUtil.getRoleFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(email);
			if (institutionName == null || institutionName.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User Institution Not Found");
			}
			if ("ADMIN".equals(role) || "TRAINER".equals(role)) {

				Batch batch = new Batch();
				batch.setBatchTitle(batchTitle);
				batch.setDurationInHours(durationInHours);
				batch.setInstitutionName(institutionName);
				Batch savedBatch = batchrepo.save(batch);

				return ResponseEntity.ok(savedBatch);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Student Cannot add Batch");
			}
		} catch (Exception e) {
			logger.error("Exception occurs in save Batch", e);
			;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// =============================Edit Batch==============================
	public ResponseEntity<?> updateBatch(Long batchId, String batchTitle, Long durationInHours,
			 String coursesJson, MultipartFile batchImage,
			String token) {
		try {


			String role = jwtUtil.getRoleFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			Optional<Muser> opmuser = muserRepo.findByEmail(email);
			if (opmuser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
			}
			Muser addinguser = opmuser.get();
			String institutionName = addinguser.getInstitutionName();
			if (!"ADMIN".equals(role) && !"TRAINER".equals(role)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Student cannot edit batch");
			}

// Find the batch by ID
			Optional<Batch> optionalBatch = batchrepo.findBatchByIdAndInstitutionName(batchId, institutionName);
			if (optionalBatch.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Batch not found");
			}

			Batch batch = optionalBatch.get();
			if (!batch.getInstitutionName().equals(institutionName)
					|| ("TRAINER".equals(role) && !addinguser.getBatches().contains(batch))) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot modify this batch");
			}

// Parse JSON data
			ObjectMapper objectMapper = new ObjectMapper();
			List<Map<String, Object>> courseList = objectMapper.readValue(coursesJson, List.class);

// Fetch and update courses
			Set<Long> newCourseIds = new HashSet<>();
			List<CourseDetail> updatedCourseDetails = new ArrayList<>();

			for (Map<String, Object> courseMap : courseList) {
				Long courseId = ((Number) courseMap.get("courseId")).longValue();
				newCourseIds.add(courseId);
				courseDetailRepository.findById(courseId).ifPresentOrElse(updatedCourseDetails::add,
						() -> logger.warn("Course with ID {} not found. Skipping...", courseId));
			}

			batch.getCourses().clear();
			batch.getCourses().addAll(updatedCourseDetails);



// Update batch details
			if (batchTitle != null && !batchTitle.isEmpty()) {
			batch.setBatchTitle(batchTitle);
			}
			if(durationInHours !=null && durationInHours<0) {
				batch.setDurationInHours(durationInHours);
			}

			if (batchImage != null && !batchImage.isEmpty()) {
				batch.setBatchImage(batchImage.getBytes());
			}

// Save updated batch
			batchrepo.save(batch);

			return ResponseEntity.ok("Batch updated successfully!");

		} catch (Exception e) {
			logger.error("Exception occurred while updating batch", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ==========================Get Batch================
	public ResponseEntity<?> GetBatch(Long id, String token) {
		try {
			String email = jwtUtil.getEmailFromToken(token);
			String role = jwtUtil.getRoleFromToken(token);
			if ("ADMIN".equals(role) || "TRAINER".equals(role)) {

				String institutionName = muserRepo.findinstitutionByEmail(email);
				if (institutionName == null) {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body("Unauthorized User Institution Not Found");
				}

				Optional<BatchDto> opbatch = batchrepo.findBatchDtoByIdAndInstitutionName(id, institutionName);
				if (opbatch.isPresent()) {
					BatchDto batch = opbatch.get();
					batch.setCourses(batchrepo.findCoursesByBatchIdAndInstitutionName(id, institutionName));
					return ResponseEntity.ok(batch);
				} else {
					return ResponseEntity.status(HttpStatus.NO_CONTENT).body("batch Not Found");
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Stdents Cannot Access This Page");

			}
		} catch (Exception e) {
			logger.error("Exception occurred while deleting batch with ID " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while Getting the batch.");
		}
	}

	// ===================Get ALl Batch===========================
	public ResponseEntity<?> GetAllBatch(String token) {
		try {
			String email = jwtUtil.getEmailFromToken(token);
			Optional<Muser> opuser = muserRepo.findByEmail(email);
			if (opuser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
			}
			Muser user = opuser.get();
			if ("ADMIN".equals(user.getRole().getRoleName())) {
				List<Batch> batches = batchrepo.findAllBatchDtosByInstitution(user.getInstitutionName());
				if (batches == null || batches.isEmpty()) { // Check for null or empty list
					return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
				}

				List<BatchDto> btc = batches.stream().map(BatchDto::new) // Convert each Batch to a DTO
						.collect(Collectors.toList());
				return ResponseEntity.ok(btc);
			} else if ("TRAINER".equals(user.getRole().getRoleName())) {
				List<Batch> batches = user.getBatches();
				List<BatchDto> btc = batches.stream().map(BatchDto::new) // Convert each Batch to a DTO
						.collect(Collectors.toList());
				return ResponseEntity.ok(btc);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Students cannot Access This Page");
			}

		} catch (Exception e) {
			logger.error("Exception occurred while getting all  batch with ID " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while getting All the batch.");
		}
	}

//=============================Get all BatchBy CourseId=================================
	public ResponseEntity<?> GetAllBatchByCourseID(String token, Long courseid) {
		try {
			String email = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(email);
			if (institutionName == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User Institution Not Found");
			}
			List<Batch> batches = batchrepo.findByCoursesCourseIdAndInstitutionName(courseid, institutionName);
			if (batches == null || batches.isEmpty()) { // Check for null or empty list
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}

			List<BatchDto> btc = batches.stream().map(BatchDto::new) // Convert each Batch to a DTO
					.collect(Collectors.toList());
			return ResponseEntity.ok(btc);

		} catch (Exception e) {
			logger.error("Exception occurred while getting batch with ID " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while Getting the batch.");
		}
	}

	// ==============================Delete Batch================================
	@Transactional
	public ResponseEntity<?> deleteBatchById(Long id, String token) {
		try {
			String role = jwtUtil.getRoleFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(email);
			if (institutionName == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User Institution Not Found");
			}
			if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
				// Check if the batch exists
				Optional<Batch> optionalBatch = batchrepo.findBatchByIdAndInstitutionName(id, institutionName);
				if (optionalBatch.isEmpty()) {
					// Return 204 No Content if batch is not found
					return ResponseEntity.noContent().build();
				}
				Batch batch = optionalBatch.get();
				// Remove the mappings for courses and trainers
				batch.getCourses().clear();

				// Save the batch to update the mappings in the database
				batchrepo.save(batch);

				// Now safely delete the batch
				batchrepo.deleteById(id);

				return ResponseEntity.ok("Batch deleted successfully!");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Students Cannot Delete Batch");
			}
		} catch (Exception e) {
			logger.error("Exception occurred while deleting batch with ID " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while deleting the batch.");
		}
	}

	public ResponseEntity<?> getCoursesoFBatch(Long batchId, String token) {
		try {
			String role = jwtUtil.getRoleFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(email);
			if (institutionName == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User Institution Not Found");
			}
			if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
				List<courseIdNameImg> courses = batchrepo.findCoursesOfBatchByBatchId(batchId, institutionName);
				return ResponseEntity.ok(courses);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Students Cannot  access This Page");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error occured in Getting courseOF batch " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//=================get batch Users======================
	public ResponseEntity<?> getUsersoFBatch(Long batchId, String token, int pageNumber, int pageSize) {
		try {
			String role = jwtUtil.getRoleFromToken(token);
			String email = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(email);
			if (institutionName == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized User Institution Not Found");
			}
			if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
				Pageable pageable = PageRequest.of(pageNumber, pageSize);
				Page<MuserDto> users = batchrepo.getMuserDetailsByBatchId(batchId, pageable);
				return ResponseEntity.ok(users);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Students Cannot Access This page");
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("error occured in Getting courseOF batch " + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//==================================searchBatchUsers==================
	public ResponseEntity<Page<MuserDto>> searchBatchUserByAdminorTrainer(String username, String email, String phone,
			LocalDate dob, String skills, int page, int size, String token, Long batchId) {
		try {
			
			String role = jwtUtil.getRoleFromToken(token);
			String emailad = jwtUtil.getEmailFromToken(token);
			String institutionName = muserRepo.findinstitutionByEmail(emailad);
			if (institutionName == null) {
				return ResponseEntity.ok(Page.empty());
			}

			if (role.equals("ADMIN") || role.equals("TRAINER")) {
				Pageable pageable = PageRequest.of(page, size);
				Page<MuserDto> Uniquestudents = batchrepo.searchUsersByBatch(batchId, username, email, phone, dob,
						institutionName, "USER", skills, pageable);
				return ResponseEntity.ok(Uniquestudents);
			} else {

				return ResponseEntity.ok(Page.empty());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			// Return an empty Page with a 200 OK status
			return ResponseEntity.ok(Page.empty());
		}
	}
//======================================PARTPAY batch====================================

		// pending
	// payments=================================================================================================
	

	// ==========================fetchimages of given batchIDs for fluter payment
	// transaction============
	public ResponseEntity<?> GetbatchImagesForMyPayments(String token, List<Long> batchIds) {
		try {
			String role = jwtUtil.getRoleFromToken(token);
			if ("USER".equals(role)) {
				List<BatchImageDTO> paymentImages = batchrepo.findBatchImagesByIds(batchIds);
				return ResponseEntity.ok(paymentImages);
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only Stdents Can Access This Page");
		} catch (Exception e) {
			logger.error("Error Getting Images of  Paymets" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
			// TODO: handle exception
		}
	}

}
