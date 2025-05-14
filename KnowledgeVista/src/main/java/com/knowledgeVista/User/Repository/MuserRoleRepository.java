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
		        pr.role_name AS parentRoleName
		    FROM 
		        muser_roles r
		    LEFT JOIN 
		        muser_roles pr ON r.parent_role_id = pr.role_id
		        WHERE 
		        r.role_name <> 'SYSADMIN'
		    """, nativeQuery = true)
		List<Map<String, Object>> fetchAllRoles();

}
