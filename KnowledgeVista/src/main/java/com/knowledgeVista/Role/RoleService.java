package com.knowledgeVista.Role;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
	@Autowired
    private RoleRepo roleRepo;

    public Roles addRole(String roleName, Long parentRoleId) {
    	roleName = roleName.toUpperCase(Locale.ROOT);

        Optional<Roles> existing = roleRepo.findByRoleNameIgnoreCase(roleName);
        if (existing.isPresent()) {
            return existing.get();
        }
        Roles parentRole = null;

        if (parentRoleId != null) {
            parentRole = roleRepo.findById(parentRoleId).orElse(null);
            if (parentRole == null) {
                throw new RuntimeException("Parent Role with ID " + parentRoleId + " not found!");
            }
        }
        Roles newRole = new Roles(roleName.toUpperCase(Locale.ROOT), parentRole);
        return roleRepo.save(newRole);
    }

    public List<Roles> getAllRoles() {
        return roleRepo.findAll();
    }

}
