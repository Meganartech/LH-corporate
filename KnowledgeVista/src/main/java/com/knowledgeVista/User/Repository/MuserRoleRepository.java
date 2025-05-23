package com.knowledgeVista.User.Repository;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.knowledgeVista.User.MuserRoles;
@Repository
public interface MuserRoleRepository extends JpaRepository<MuserRoles,Long> {
	@Query("SELECT u FROM MuserRoles u WHERE u.roleName = ?1")
	MuserRoles findByroleName(String roleName);
	@Query("SELECT u FROM MuserRoles u WHERE u.roleName = ?1")
	Optional<MuserRoles> findByRoleName(String roleName);
	
	Optional<MuserRoles> findByRoleNameIgnoreCase(String roleName);
	@Query(value = """
		    SELECT 
		        r.role_id AS roleId,
		        r.role_name AS roleName,
		        r.is_active AS isActive,
		        pr.role_id AS parentRoleId,
		        pr.role_name AS parentRoleName,
		        STRING_AGG(DISTINCT b.batch_title, ', ') AS batchNames
		    FROM 
		        muser_roles r
		    LEFT JOIN 
		        muser_roles pr ON r.parent_role_id = pr.role_id
		    LEFT JOIN 
		        batch_role_mapping brm ON brm.role_id = r.role_id
		    LEFT JOIN 
		        batch b ON brm.batch_id = b.id
		    WHERE 
		        r.role_name <> 'SYSADMIN'
		    GROUP BY 
		        r.role_id, r.role_name, r.is_active, pr.role_id, pr.role_name
		    """, nativeQuery = true)
		List<Map<String, Object>> fetchAllRoles();



}
