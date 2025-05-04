package com.knowledgeVista.Role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Roles {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(unique = true,nullable = false)
    private String roleName;
    @ManyToOne
    @JoinColumn(name="parent_role_id")
    @JsonIgnore
    private Roles parentRole;
    @Transient
    private Long parentRoleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Roles getParentRole() {
        return parentRole;
    }

    public void setParentRole(Roles parentRole) {
        this.parentRole = parentRole;
    }
    public Long getParentRoleId() {
        return parentRole != null ? parentRole.getRoleId() : null;
    }
    public Roles(String roleName, Roles parentRole) {
        this.roleName = roleName;
        this.parentRole = parentRole;
    }
    public Roles(Long roleId, String roleName, Roles parentRole) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.parentRole = parentRole;
    }

}
