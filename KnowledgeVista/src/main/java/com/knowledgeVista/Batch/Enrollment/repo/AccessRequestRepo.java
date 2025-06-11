package com.knowledgeVista.Batch.Enrollment.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.knowledgeVista.Batch.Enrollment.AccessRequest;
import com.knowledgeVista.Batch.Enrollment.AccessRequestDTO;

@Repository
public interface AccessRequestRepo extends JpaRepository<AccessRequest, Long>{

	
	@Query("SELECT a FROM AccessRequest a WHERE a.user.userId=:userId AND a.batch.id=:batchId")
	Optional<AccessRequest> findByUserIdAndBatchId(Long userId, Long batchId);

	@Query("SELECT new com.knowledgeVista.Batch.Enrollment.AccessRequestDTO(" +
	        "ar.id, u.userId, u.username, b.id, b.batchTitle, ar.requestTime, ar.status) " +
	        "FROM AccessRequest ar " +
	        "JOIN ar.user u " +
	        "JOIN ar.batch b " +
	        "WHERE ar.status = 'PENDING'")
	List<AccessRequestDTO> findPendingAccessRequestDTOs();
	
	
	@Query("SELECT r FROM AccessRequest r WHERE r.id=:id AND r.status='PENDING'")
	Optional<AccessRequest>findByIdAndStatusNotPenging(@Param("id") Long id );



}
