package com.knowledgeVista.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter@Setter@NoArgsConstructor
public class MuserDto {
	 private Long userId;
	    private String username;
	    private String email;
	    private String phone;
	    private Boolean isActive;
	    private LocalDate dob;
	    private String skills;
	    private String institutionName;
	    private LocalDateTime LastActive;
	    private String roleName;

	    public MuserDto(Long userId, String username, String email, String phone, Boolean isActive, LocalDate dob, String skills,String institutionName) {
	        this.userId = userId;
	        this.username = username;
	        this.email = email;
	        this.phone = phone;
	        this.isActive = isActive;
	        this.dob = dob;
	        this.skills = skills;
	        this.institutionName=institutionName;
	    }
	    
	    public MuserDto(Long userId, String username, String email, String phone, Boolean isActive, LocalDate dob, String skills) {
	        this.userId = userId;
	        this.username = username;
	        this.email = email;
	        this.phone = phone;
	        this.isActive = isActive;
	        this.dob = dob;
	        this.skills = skills;
	    }
	    
	    public MuserDto(Long userId, String username, String email, String phone, Boolean isActive, LocalDate dob, String skills, String institutionName, String roleName) {
	        this.userId = userId;
	        this.username = username;
	        this.email = email;
	        this.phone = phone;
	        this.isActive = isActive;
	        this.dob = dob;
	        this.skills = skills;
	        this.institutionName = institutionName;
	        this.roleName = roleName;
	    }
	    
	    public MuserDto(Muser muser) {
	        this.userId = muser.getUserId();
	        this.username = muser.getUsername();
	        this.email = muser.getEmail();
	        this.phone = muser.getPhone();
	        this.isActive = muser.getIsActive();
	        this.dob = muser.getDob();
	        this.skills = muser.getSkills();
	        this.institutionName = muser.getInstitutionName();
	        this.LastActive = muser.getLastactive();
	        this.roleName = muser.getRole() != null ? muser.getRole().getRoleName() : null;
	    }
}
