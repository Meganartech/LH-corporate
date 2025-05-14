package com.knowledgeVista.Batch.Enrollment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.knowledgeVista.Batch.Enrollment.BatchEnrollment;

@Repository
public interface BatchEnrollmentRepo extends JpaRepository<BatchEnrollment, Long>{

}
