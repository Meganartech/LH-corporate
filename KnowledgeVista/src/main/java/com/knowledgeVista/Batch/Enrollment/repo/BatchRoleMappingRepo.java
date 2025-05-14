package com.knowledgeVista.Batch.Enrollment.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.knowledgeVista.Batch.Enrollment.BatchRoleMapping;

@Repository
public interface BatchRoleMappingRepo extends JpaRepository<BatchRoleMapping, Long> {

	@Query("SELECT b FROM BatchRoleMapping b WHERE b.role.roleId=:roleId")
	List<BatchRoleMapping>findbyRoleId(Long roleId);
}
