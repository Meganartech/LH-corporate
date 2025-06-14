package com.knowledgeVista.Batch.Repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.Batch.BatchDto;
import com.knowledgeVista.Batch.BatchImageDTO;
import com.knowledgeVista.Batch.CourseDto;
import com.knowledgeVista.Batch.SearchDto;
import com.knowledgeVista.Batch.TrainerDto;
import com.knowledgeVista.Course.CourseDetailDto.courseIdNameImg;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.MuserDto;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {

	@Query("SELECT b FROM Batch b WHERE b.id = :id AND b.institutionName = :institutionName")
	Optional<Batch> findBatchByIdAndInstitutionName(@Param("id") Long id,
			@Param("institutionName") String institutionName);

	@Query("SELECT b FROM Batch b WHERE b.batchId = :id AND b.institutionName = :institutionName")
	Optional<Batch> findBatchByIdAndInstitutionName(@Param("id") String id,
			@Param("institutionName") String institutionName);

	@Query("SELECT new com.knowledgeVista.Course.CourseDetailDto$courseIdNameImg(c.courseId, c.courseName, c.courseImage) "
			+ "FROM Batch b " + "LEFT JOIN b.courses c " + "WHERE b.id = :id AND b.institutionName = :institutionName")
	List<courseIdNameImg> findCoursesOfBatchByBatchId(@Param("id") Long id,
			@Param("institutionName") String institutionName);

	@Query("SELECT b FROM Batch b JOIN b.courses c WHERE c.courseId = :courseId AND b.institutionName = :institutionName")
	List<Batch> findByCoursesCourseIdAndInstitutionName(@Param("courseId") Long courseId,
			@Param("institutionName") String institutionName);

	@Query("SELECT new com.knowledgeVista.Batch.BatchDto(" +
		       "b.id, b.batchId, b.batchTitle, b.institutionName, b.BatchImage, b.durationInHours) " +
		       "FROM Batch b " +
		       "WHERE b.id = :id AND b.institutionName = :institutionName")
		Optional<BatchDto> findBatchDtoByIdAndInstitutionName(@Param("id") Long id,
		                                                      @Param("institutionName") String institutionName);



	@Query("SELECT new com.knowledgeVista.Batch.CourseDto(c.courseId, c.courseName,c.Duration) " + "FROM Batch b "
			+ "JOIN b.courses c " + "WHERE b.id = :id AND b.institutionName = :institutionName")
	List<CourseDto> findCoursesByBatchIdAndInstitutionName(@Param("id") Long id,
			@Param("institutionName") String institutionName);



	@Query("SELECT b FROM Batch b  WHERE b.institutionName = :institutionName")
	List<Batch> findAllBatchDtosByInstitution(@Param("institutionName") String institutionName);

	@Query("SELECT new com.knowledgeVista.Batch.SearchDto(u.id, u.batchTitle, 'BATCH') " + "FROM Batch u "
			+ "WHERE u.institutionName = :institutionName "
			+ "AND LOWER(u.batchTitle) LIKE LOWER(CONCAT('%', :Query, '%'))")
	List<SearchDto> findbatchAsSearchDto(@Param("Query") String Query,
			@Param("institutionName") String institutionName);

	@Query("SELECT b.id, b.batchId, b.batchTitle " + "FROM Batch b "
			+ "WHERE (:batchtitle IS NOT NULL AND :batchtitle <> '' AND LOWER(b.batchTitle) LIKE LOWER(CONCAT('%', :batchtitle, '%'))) "
			+ "AND (:institutionName IS NOT NULL AND :institutionName <> '' AND LOWER(b.institutionName) = LOWER(:institutionName))")
	List<Object[]> searchBatchByBatchNameAndInstitution(@Param("batchtitle") String batchtitle,
			@Param("institutionName") String institutionName);

	@Query("SELECT c.id FROM Batch b " + "JOIN b.courses c " + "WHERE b.id = :id")
	List<Long> findCourseIdsByBatchId(@Param("id") Long id);

	@Query("SELECT t.email FROM Batch b " + "JOIN b.trainers t " + "WHERE b.id = :id")
	List<String> findtrainersByBatchId(@Param("id") Long id);

	@Query("SELECT e.user.email FROM BatchEnrollment e WHERE e.batch.id = :batchId")
	List<String> findUserEmailsByBatchId(@Param("batchId") Long batchId);


	@Query("SELECT e.user FROM BatchEnrollment e WHERE e.batch.id = :batchId")
	List<Muser> findAllUsersByBatchId(@Param("batchId") Long batchId);


	@Query("SELECT COUNT(e.user) FROM BatchEnrollment e WHERE e.batch.id = :batchId")
	Long countAllUsersByBatchId(@Param("batchId") Long batchId);


	@Query("SELECT new com.knowledgeVista.User.MuserDto(" +
		       "e.user.userId, e.user.username, e.user.email, e.user.phone, " +
		       "e.user.isActive, e.user.dob, e.user.skills, e.user.institutionName) " +
		       "FROM BatchEnrollment e " +
		       "WHERE e.batch.id = :batchId")
		Page<MuserDto> getMuserDetailsByBatchId(@Param("batchId") Long batchId, Pageable pageable);


	@Query("SELECT new com.knowledgeVista.User.MuserDto(" + "m.userId, " + "m.username, " + "m.email, " + "m.phone, "
			+ "m.isActive, " + "m.dob, " + "m.skills," + "m.institutionName) " + "FROM Muser m "
			+ "JOIN m.enrolledbatch b " + "WHERE b.id = :batchId AND "
			+ "( (:username IS NULL OR LOWER(m.username) LIKE LOWER(CONCAT(:username, '%'))) AND "
			+ "(:email IS NULL OR LOWER(m.email) LIKE LOWER(CONCAT(:email, '%'))) AND "
			+ "(:phone IS NULL OR LOWER(m.phone) LIKE LOWER(CONCAT(:phone, '%'))) AND "
			+ "(:dob IS NULL OR m.dob = :dob) AND " + "(m.institutionName = :institutionName) AND "
			+ "(m.role.roleName = :roleName) AND "
			+ "(:skills IS NULL OR LOWER(m.skills) LIKE LOWER(CONCAT(:skills, '%'))) )")
	Page<MuserDto> searchUsersByBatch(@Param("batchId") Long batchId, @Param("username") String username,
			@Param("email") String email, @Param("phone") String phone, @Param("dob") LocalDate dob,
			@Param("institutionName") String institutionName, @Param("roleName") String roleName,
			@Param("skills") String skills, Pageable pageable);

	@Query(value = "SELECT b.id AS id, " + "b.batch_id AS batchId, " + "b.batch_title AS batchTitle, "
			+ "b.institution_name AS institutionName, "
			+ "COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, "
			+ "COALESCE(STRING_AGG(DISTINCT t.username, ','), '') AS trainerNames, " + "b.batch_image AS batchImage, "
			+ "TO_CHAR(b.start_date, 'Mon') || ' to ' || TO_CHAR(b.end_date, 'Mon') AS duration " + "FROM batch b "
			+ "LEFT JOIN batch_courses bc ON b.id = bc.batch_id "
			+ "LEFT JOIN course_detail c ON c.course_id = bc.course_id "
			+ "LEFT JOIN batch_trainers bt ON b.id = bt.batch_id " + "LEFT JOIN muser t ON t.user_id = bt.user_id "
			+ "WHERE bt.user_id = :trainerId " + // Fetch batches assigned to the trainer
			"GROUP BY b.id, b.batch_id, b.batch_title, b.start_date, b.end_date, "
			+ "b.institution_name, b.no_of_seats " 
			+ "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Map<String, Object>> findAssignedBatchesForTrainerIdWithPagination(@Param("trainerId") Long trainerId,
			@Param("pageSize") int pageSize, @Param("offset") int offset);

	@Query(value = "SELECT COUNT(DISTINCT b.id) FROM batch b " + "LEFT JOIN batch_trainers bt ON b.id = bt.batch_id "
			+ "WHERE bt.user_id = :trainerId", nativeQuery = true)
	long countAssignedBatchesForTrainerId(@Param("trainerId") Long trainerId);

	@Query(value = "SELECT b.id AS id, "
			+ "b.batch_id AS batchId, b.batch_title AS batchTitle "
			+ " b.institution_name AS institutionName "
			 + "COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, "
			+ "COALESCE(STRING_AGG(DISTINCT t.username, ','), '') AS trainerNames, "
			+ "b.batch_image AS batchImage, "
			+ "TO_CHAR(b.start_date, 'Mon') || ' to ' || TO_CHAR(b.end_date, 'Mon') AS duration, "
			+ "(SELECT COUNT(bu.user_id) FROM batch_users bu WHERE bu.batch_id = b.id) AS enrolledUsers "
			+ "FROM batch b " + "LEFT JOIN batch_courses bc ON b.id = bc.batch_id "
			+ "LEFT JOIN course_detail c ON c.course_id = bc.course_id "
			+ "LEFT JOIN batch_trainers bt ON b.id = bt.batch_id " + "LEFT JOIN muser t ON t.user_id = bt.user_id "
			+ "JOIN muser u ON u.user_id = :trainerId " + "WHERE b.institution_name = u.institution_name "
			+ "AND NOT EXISTS (" + "    SELECT 1 FROM batch_trainers bt2 "
			+ "    WHERE bt2.batch_id = b.id AND bt2.user_id = :trainerId" + ") "
			+ "GROUP BY b.id, b.batch_id, b.batch_title, "
			+ "b.institution_name" 
			+ "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
	List<Map<String, Object>> findBatchesNotAssignedForTrainerIdWithPagination(@Param("trainerId") Long trainerId,
			@Param("pageSize") int pageSize, @Param("offset") int offset);

	@Query(value = "SELECT COUNT(DISTINCT b.id) FROM batch b " + "LEFT JOIN batch_trainers bt ON b.id = bt.batch_id "
			+ "WHERE (bt.user_id IS NULL OR bt.user_id != :trainerId)", nativeQuery = true)
	long countBatchesNotAssignedForTrainerId(@Param("trainerId") Long trainerId);

	// =================below For users====================
	@Query(value = 
		    "SELECT " +
		    "    b.id AS id, " +
		    "    b.batch_id AS batchId, " +
		    "    b.batch_title AS batchTitle, " +
		    "    b.duration_in_hours AS durationInHours, " +
		    "    b.institution_name AS institutionName, " +
		    "    COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, " +
		    "    b.batch_image AS batchImage " +
		    "FROM batch b " +
		    "JOIN batch_enrollment be ON be.batch_id = b.id " +
		    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
		    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
		    "WHERE be.user_id = :userId " +
		    "AND b.institution_name = :institutionName " +
		    "GROUP BY b.id, b.batch_id, b.batch_title, b.duration_in_hours, b.institution_name, b.batch_image " +
		    "ORDER BY b.batch_id DESC " +
		    "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
		List<Map<String, Object>> findBatchesEnrolledByUser(
		    @Param("userId") Long userId,
		    @Param("institutionName") String institutionName,
		    @Param("pageSize") int pageSize,
		    @Param("offset") int offset);


		@Query(value = "SELECT COUNT(DISTINCT b.id) " +
		    "FROM batch b " +
		    "JOIN batch_enrollment be ON be.batch_id = b.id " +
		    "WHERE be.user_id = :userId " +
		    "AND b.institution_name = :institutionName", nativeQuery = true)
		Long countBatchesEnrolledByUser(
		    @Param("userId") Long userId,
		    @Param("institutionName") String institutionName);


		@Query(value = 
			    "SELECT " +
			    "    b.id AS id, " +
			    "    b.batch_id AS batchId, " +
			    "    b.batch_title AS batchTitle, " +
			    "    b.duration_in_hours AS durationInHours, " +
			    "    b.institution_name AS institutionName, " +
			    "    COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, " +
			    "    b.batch_image AS batchImage " +
			    "FROM batch b " +
			    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
			    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
			    "WHERE NOT EXISTS ( " +
			    "    SELECT 1 FROM batch_enrollment be " +
			    "    WHERE be.batch_id = b.id AND be.user_id = :userId " +
			    ") " +
			    "AND b.institution_name = :institutionName " +
			    "GROUP BY b.id, b.batch_id, b.batch_title, b.duration_in_hours, b.institution_name, b.batch_image " +
			    "ORDER BY b.batch_id DESC " +
			    "LIMIT :pageSize OFFSET :offset",
			    nativeQuery = true)
			List<Map<String, Object>> findBatchesNotEnrolledByUser(
			    @Param("userId") Long userId,
			    @Param("institutionName") String institutionName,
			    @Param("pageSize") int pageSize,
			    @Param("offset") int offset);



		@Query(value = 
			    "SELECT COUNT(DISTINCT b.id) " +
			    "FROM batch b " +
			    "WHERE NOT EXISTS ( " +
			    "    SELECT 1 FROM batch_enrollment be " +
			    "    WHERE be.batch_id = b.id AND be.user_id = :userId " +
			    ") " +
			    "AND b.institution_name = :institutionName",
			    nativeQuery = true)
			long countBatchesNotEnrolledByUser(
			    @Param("userId") Long userId,
			    @Param("institutionName") String institutionName);


		@Query("SELECT new com.knowledgeVista.Batch.BatchImageDTO(b.id,b.BatchImage) FROM Batch b WHERE b.id IN :ids")
	List<BatchImageDTO> findBatchImagesByIds(@Param("ids") List<Long> ids);
	
	
	
	//getting for RoleBased ALlocation
	@Query(value = "SELECT " +
		    "b.id AS id, " +
		    "b.batch_id AS batchId, " +
		    "b.batch_title AS batchTitle, " +
		    "b.duration_in_hours AS durationInHours,"+
		    "b.institution_name AS institutionName, " +
		    "COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, " +
		    "b.batch_image AS batchImage, " +
		    "(SELECT COUNT(bu.user_id) FROM batch_users bu WHERE bu.batch_id = b.id) AS enrolledUsers " +
		    "FROM batch b " +
		    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
		    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
		    "WHERE b.institution_name = :institutionName " +
		    "AND NOT EXISTS ( " +
		        "SELECT 1 FROM batch_role_mapping brm WHERE brm.batch_id = b.id AND brm.role_id = :roleId " +
		    ") " +
		    "GROUP BY b.id, b.batch_id, b.batch_title, b.institution_name, b.batch_image " +
		    "ORDER BY b.batch_id DESC " +
		    "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
		List<Map<String, Object>> findBatchesNotMappedToRoleInInstitution(
		    @Param("roleId") Long roleId,
		    @Param("institutionName") String institutionName,
		    @Param("pageSize") int pageSize,
		    @Param("offset") int offset);


	@Query(value =
		    "SELECT COUNT(DISTINCT b.id) " +
		    "FROM batch b " +
		    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
		    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
		    "WHERE b.institution_name = :institutionName " +
		    "AND NOT EXISTS ( " +
		    "    SELECT 1 FROM batch_role_mapping brm WHERE brm.batch_id = b.id AND brm.role_id = :roleId " +
		    ")",
		    nativeQuery = true)
		long countBatchesNotMappedToRoleInInstitution(
		    @Param("roleId") Long roleId,
		    @Param("institutionName") String institutionName);
	
	
	//getting for RoleBased ALlocation
	@Query(value = "SELECT " +
		    "b.id AS id, " +
		    "b.batch_id AS batchId, " +
		    "b.batch_title AS batchTitle, " +
		    "b.duration_in_hours AS durationInHours,"+
		    "b.institution_name AS institutionName, " +
		    "COALESCE(STRING_AGG(DISTINCT c.course_name, ','), '') AS courseNames, " +
		    "b.batch_image AS batchImage, " +
		    "(SELECT COUNT(bu.user_id) FROM batch_users bu WHERE bu.batch_id = b.id) AS enrolledUsers " +
		    "FROM batch b " +
		    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
		    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
		    "WHERE b.institution_name = :institutionName " +
		    "AND  EXISTS ( " +
		        "SELECT 1 FROM batch_role_mapping brm WHERE brm.batch_id = b.id AND brm.role_id = :roleId " +
		    ") " +
		    "GROUP BY b.id, b.batch_id, b.batch_title, b.institution_name, b.batch_image " +
		    "ORDER BY b.batch_id DESC " +
		    "LIMIT :pageSize OFFSET :offset", nativeQuery = true)
		List<Map<String, Object>> findBatchesMappedToRoleInInstitution(
		    @Param("roleId") Long roleId,
		    @Param("institutionName") String institutionName,
		    @Param("pageSize") int pageSize,
		    @Param("offset") int offset);
	
	
	@Query(value =
		    "SELECT COUNT(DISTINCT b.id) " +
		    "FROM batch b " +
		    "LEFT JOIN batch_courses bc ON b.id = bc.batch_id " +
		    "LEFT JOIN course_detail c ON c.course_id = bc.course_id " +
		    "WHERE b.institution_name = :institutionName " +
		    "AND  EXISTS ( " +
		    "    SELECT 1 FROM batch_role_mapping brm WHERE brm.batch_id = b.id AND brm.role_id = :roleId " +
		    ")",
		    nativeQuery = true)
		long countBatchesMappedToRoleInInstitution(
		    @Param("roleId") Long roleId,
		    @Param("institutionName") String institutionName);



}
