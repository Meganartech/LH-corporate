package com.knowledgeVista.User;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter@NoArgsConstructor
public class MuserRoles {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long roleId;
	    private String roleName;
	    private Boolean isActive;
	    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
	    @JsonIgnore
	    private List<Muser> users;
	    @ManyToOne
	    @JoinColumn(name="parent_role_id")
	    @JsonIgnore
	    private MuserRoles parentRole;
	    @Transient
	    private Long parentRoleId;
	    public MuserRoles(String roleName, MuserRoles parentRole) {
	        this.roleName = roleName;
	        this.parentRole = parentRole;
	    }
	    public MuserRoles(Long roleId, String roleName, MuserRoles parentRole) {
	        this.roleId = roleId;
	        this.roleName = roleName;
	        this.parentRole = parentRole;
	    }
	    
	    
}
