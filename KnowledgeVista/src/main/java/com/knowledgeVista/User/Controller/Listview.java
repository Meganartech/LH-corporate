package com.knowledgeVista.User.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.knowledgeVista.Batch.SearchDto;
import com.knowledgeVista.Batch.Enrollment.service.BatchEnrollmentService;
import com.knowledgeVista.Batch.Repo.BatchRepository;
import com.knowledgeVista.Email.EmailService;
import com.knowledgeVista.User.Muser;
import com.knowledgeVista.User.MuserDto;
import com.knowledgeVista.User.MuserRequiredDto;
import com.knowledgeVista.User.Approvals.MuserApprovalPageable;
import com.knowledgeVista.User.Approvals.MuserApprovalRepo;
import com.knowledgeVista.User.Approvals.MuserApprovals;
import com.knowledgeVista.User.Repository.MuserRepoPageable;
import com.knowledgeVista.User.Repository.MuserRepositories;
import com.knowledgeVista.User.Repository.MuserRoleRepository;
import com.knowledgeVista.User.SecurityConfiguration.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class Listview {
	@Autowired
	private MuserRepositories muserrepositories;
	@Autowired
	private MuserRoleRepository roleRepo;
	@Autowired
	private BatchEnrollmentService BatchenrollService;
	 @Autowired
	 private JwtUtil jwtUtil;
	 @Autowired 
	 private MuserRepoPageable muserPageRepo;
	
	 @Autowired
	 private MuserApprovalRepo MuserApproval;
	@Autowired
	private MuserApprovalPageable approvalpage;
	@Autowired
	private BatchRepository batchrepo;
	@Autowired
	private EmailService emailservice;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	 private static final Logger logger = LoggerFactory.getLogger(Listview.class);

	

//```````````````WORKING````````````````````````````````````

    public ResponseEntity<Page<MuserDto>> getUsersByRoleName(String token ,int pageNumber,int pageSize) {
        try {
   	     String role = jwtUtil.getRoleFromToken(token);
   	     String roleu="USER";
   	     String institution=jwtUtil.getInstitutionFromToken(token);
   	     
   	     if("ADMIN".equals(role)||"TRAINER".equals(role)){
   	    	Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<MuserDto> users = muserPageRepo.findByRoleNameAndInstitutionName(roleu, institution,pageable);
            return ResponseEntity.ok(users);
   	     }else {

   	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
   	     }
        } catch (Exception e) {
        	e.printStackTrace();
        	 logger.error("", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    //**************************Getting Dynamic Role***********************
    public ResponseEntity<Page<MuserDto>> getDynamicUsersByRoleName(String token, int pageNumber, int pageSize, String roleu) {
        try {
            String role = jwtUtil.getRoleFromToken(token);
            String institution = jwtUtil.getInstitutionFromToken(token);
            if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
                Pageable pageable = PageRequest.of(pageNumber, pageSize);
                Page<MuserDto> users = muserPageRepo.findByRoleNameAndInstitutionName(roleu, institution, pageable);
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error fetching users by role", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Fetch one user by role and email for the logged‑in admin / trainer.
     */
    public ResponseEntity<MuserDto> getDynamicUserByRoleNameAndEmail(
            String token,
            String roleu,          // role we’re looking for, e.g. "PRINCIPAL"
            String targetEmail) {  // email we want the data for

        try {
           
            String callerRole  = jwtUtil.getRoleFromToken(token);   // ADMIN / TRAINER / …
            String callerEmail = jwtUtil.getEmailFromToken(token);

            /* ───── 2. Institution & admin‑is‑active check ───── */
            Optional<Muser> callerOpt = muserrepositories.findByEmail(callerEmail);
            if (callerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            /* ───── 3. Authorisation: only ADMIN or TRAINER can fetch ───── */
            if (!"ADMIN".equals(callerRole) && !"TRAINER".equals(callerRole)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            /* ───── 4. Fetch user by role & email ───── */
            MuserDto user = muserPageRepo.findByRoleNameAndEmail(roleu, targetEmail)   // no pageable → single result
                             .orElse(null);                                // method should return Optional

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            /* ───── 5. Return success ───── */
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            logger.error("Error fetching user by role and email", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    
//    public ResponseEntity<Page<MuserDto>> getDynamicUserByRoleNameAndEmail(String token, int pageNumber, int pageSize, String roleu) {
//        try {
//            // Validate the JWT token
//            if (!jwtUtil.validateToken(token)) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//
//            // Extract role and email from the token
//            String role = jwtUtil.getRoleFromToken(token);
//            String email = jwtUtil.getEmailFromToken(token);
//            
//            // Check if the user exists based on email
//            Optional<Muser> opreq = muserrepositories.findByEmail(email);
//            String institution = "";
//
//            // If user exists, get the institution name and check if the admin is active
//            if (opreq.isPresent()) {
//                Muser requser = opreq.get();
//                institution = requser.getInstitutionName();
//                boolean adminIsactive = muserrepositories.getactiveResultByInstitutionName("ADMIN", institution);
//                
//                // If the admin is not active, return unauthorized
//                if (!adminIsactive) {
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//                }
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//
//            // Check if the role is either ADMIN or TRAINER
//            if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
//                Pageable pageable = PageRequest.of(pageNumber, pageSize);
//                // Fetch users by role name and email using the repository
//                Page<MuserDto> users =  muserPageRepo.findByRoleNameAndEmail(roleu, email, pageable);
//                return ResponseEntity.ok(users);
//            } else {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("Error fetching users by role and email", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
    
    public ResponseEntity<Page<MuserDto>> getUsersByRoleId(String token, int pageNumber, int pageSize, Long roleId) {
        try {
            String userEmail = jwtUtil.getEmailFromToken(token);
            Optional<Muser> currentUser = muserrepositories.findByEmail(userEmail);

            if (currentUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Muser loggedInUser = currentUser.get();
            String institution = loggedInUser.getInstitutionName();
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<MuserDto> users = muserPageRepo.findByRoleIdAndInstitutionName(roleId, institution, pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error fetching users by role ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    public ResponseEntity<Page<MuserDto>> GetStudentsOfTrainer(String token,int page, int size) {
        try {
   	     String role = jwtUtil.getRoleFromToken(token);
   	     String email=jwtUtil.getEmailFromToken(token);
   	     if("TRAINER".equals(role)){
       	        Pageable pageable = PageRequest.of(page, size);
       	    	Page<MuserDto> Uniquestudents=muserPageRepo.findStudentsOfTrainer(email, pageable);
            return ResponseEntity.ok(Uniquestudents);
   	     }else {

   	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
   	     }
   	    
        } catch (Exception e) {
        	e.printStackTrace();
        	 logger.error("", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }  
    
    
    
    
//```````````````WORKING````````````````````````````````````
    
    public ResponseEntity<?> getUserById( Long userId,String token) {
        try {
      	     String role = jwtUtil.getRoleFromToken(token);
	   	   String institution=jwtUtil.getInstitutionFromToken(token);
      	     if("ADMIN".equals(role)||"TRAINER".equals(role)){
           Optional<MuserRequiredDto> opuser = muserrepositories.findByuserIdandInstitutionName(userId, institution);
         if(opuser.isPresent()) {
        	 MuserRequiredDto user=opuser.get();
            return ResponseEntity.ok(user);
         }else {
        	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
         }
      	   }else {

     	         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
     	     }
        } catch (Exception e) {
        	e.printStackTrace();
        	 logger.error("", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
//``````````````````````````Approvals`````````````````````````````````
    public ResponseEntity<Page<MuserDto>>getallApprovals( String token ,int pageNumber,int pageSize){
    	 try {
    		     String role = jwtUtil.getRoleFromToken(token);
    		     if("ADMIN".equals(role)){
    		    	 Pageable pageable = PageRequest.of(pageNumber, pageSize);
    		    		 Page<MuserDto> users = approvalpage.findAllUsers(pageable);
    	        return ResponseEntity.ok(users);
    	        
    		     }else {
    		    	  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    		     }
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    	    }

    }
//```````````````WORKING````````````````````````````````````
public ResponseEntity<Page<MuserDto>> getTrainerByRoleName( String token ,int pageNumber,int pageSize) {
	
    try {
	     String role = jwtUtil.getRoleFromToken(token);
	     String roleu="TRAINER";
   	     String institution=jwtUtil.getInstitutionFromToken(token);
	     if("ADMIN".equals(role)){
	    	 Pageable pageable = PageRequest.of(pageNumber, pageSize);
	    		 Page<MuserDto> users = muserPageRepo.findByRoleNameAndInstitutionName(roleu, institution,pageable);
        return ResponseEntity.ok(users);
        
	     }else {
	    	  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	     }
    } catch (Exception e) {
    	e.printStackTrace();
    	 logger.error("", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

public ResponseEntity<Page<MuserDto>> getStudentByRoleName(String token, int pageNumber, int pageSize) {
    try {
        String role = jwtUtil.getRoleFromToken(token);
        String roleu = "STUDENT";
       String institution=jwtUtil.getInstitutionFromToken(token);
        if ("ADMIN".equals(role)) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<MuserDto> users = muserPageRepo.findByRoleNameAndInstitutionName(roleu, institution, pageable);
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    } catch (Exception e) {
        e.printStackTrace();
        logger.error("", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

public ResponseEntity< List<?>> SearchEmail(String token,String Query){
	try {
		 String email=jwtUtil.getEmailFromToken(token);
   	     String institutionname =muserrepositories.findinstitutionByEmail(email);
   	     
   	  if (institutionname != null && !institutionname.isEmpty()) {
   	   
   	    	List<SearchDto> list1= muserrepositories.findEmailsAsSearchDto(Query, institutionname);
   	    	List<SearchDto> list3 = batchrepo.findbatchAsSearchDto(Query, institutionname);
   	    	list1.addAll(list3);
   	    	return ResponseEntity.ok(list1);
   	     }else {
   	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
   	     }
		
	}catch(Exception e) {
		e.printStackTrace();
		 logger.error("", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	}
}

public ResponseEntity< List<String>> SearchEmailTrainer(String token,String query){
	try {
		 String email=jwtUtil.getEmailFromToken(token);
   	     Optional<Muser>opreq=muserrepositories.findByEmail(email);
   	     
   	     if(opreq.isPresent()) {
   	    	 Muser requser=opreq.get();
   	    	requser.getInstitutionName();
   	    	if(requser.getRole().getRoleName().equals("TRAINER")) {
   	    	List<String> listu= muserrepositories.findEmailsInAllotedCoursesByUserEmail(email, query);
   	    	return ResponseEntity.ok(listu);
   	    	}else {
   	    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
   	    	}
   	     }else {
   	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
   	     }
		
	}catch(Exception e) {
		e.printStackTrace();
		 logger.error("", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	}
}
//===============================SYSADMIN==============================

public ResponseEntity<Page<MuserDto>> searchUser( String username, String email, String phone, LocalDate dob, String institutionName,
       String skills, int page, int size,String token
        ) {
	try{
		 String role=jwtUtil.getRoleFromToken(token);
		 if(role.equals("SYSADMIN")) {
	    Pageable pageable = PageRequest.of(page, size);
	Page<MuserDto> Uniquestudents=muserPageRepo.CustomesearchUsers(username, email, phone,dob, institutionName, "USER",skills, pageable);
	return ResponseEntity.ok(Uniquestudents);
		 }else {

			  return ResponseEntity.ok(Page.empty());
		 }
	}catch (Exception e) {
	    e.printStackTrace();
	    logger.error("", e);
	    // Return an empty Page with a 200 OK status
	    return ResponseEntity.ok(Page.empty());
	}
}

public ResponseEntity<Page<MuserDto>> searchTrainer( String username, String email, String phone, LocalDate dob, String institutionName,
       String skills, int page, int size,String token
        ) {
	try{
		 String role=jwtUtil.getRoleFromToken(token);
		 if(role.equals("SYSADMIN")) {
	    Pageable pageable = PageRequest.of(page, size);
	Page<MuserDto> Uniquestudents=muserPageRepo.CustomesearchUsers(username, email, phone,dob, institutionName, "TRAINER",skills, pageable);
	return ResponseEntity.ok(Uniquestudents);
		 }else {

			  return ResponseEntity.ok(Page.empty());
		 }
	}catch (Exception e) {
	    e.printStackTrace();
	    // Return an empty Page with a 200 OK status
	    logger.error("", e);
	    return ResponseEntity.ok(Page.empty());
	}
}
public ResponseEntity<Page<MuserDto>> searchAdmin( String username, String email, String phone, LocalDate dob, String institutionName,
       String skills, int page, int size,String token
        ) {
try{
	 String role=jwtUtil.getRoleFromToken(token);
	 if(role.equals("SYSADMIN")) {
    Pageable pageable = PageRequest.of(page, size);
Page<MuserDto> Uniquestudents=muserPageRepo.CustomesearchUsers(username, email, phone,dob, institutionName, "ADMIN",skills, pageable);
return ResponseEntity.ok(Uniquestudents);
	 }else {

		  return ResponseEntity.ok(Page.empty());
	 }
}catch (Exception e) {
    e.printStackTrace();
    logger.error("", e);
    // Return an empty Page with a 200 OK status
    return ResponseEntity.ok(Page.empty());
}
}

//===============================SYSADMIN==============================

//=================================ADMIN SEARCH============================
public ResponseEntity<Page<MuserDto>> searchApprovalByAdmin( String username, String email, String phone, LocalDate dob,
	       String skills, String roleName,int page, int size,String token
	        ) {
		try{
			 String adminemail=jwtUtil.getEmailFromToken(token);
			 Optional<Muser>opmuser=muserrepositories.findByEmail(adminemail);
			 if(opmuser.isPresent()) {
				 Muser user= opmuser.get();
				 String role=user.getRole().getRoleName();
			 if(role.equals("ADMIN")) {
				 String institutionName= user.getInstitutionName();
		    Pageable pageable = PageRequest.of(page, size);
		Page<MuserDto> Uniquestudents=approvalpage.CustomeApprovalsearchForAdmin(username, email, phone, dob, institutionName,  skills, pageable);
		return ResponseEntity.ok(Uniquestudents);
			 }else {

				  return ResponseEntity.ok(Page.empty());
			 }
			 }else {
				  return ResponseEntity.ok(Page.empty());
			 }
		}catch (Exception e) {
		    e.printStackTrace();
		    // Return an empty Page with a 200 OK status
		    return ResponseEntity.ok(Page.empty());
		}
	}
public ResponseEntity<?>RejectUser(Long id,String token){
	try{
		 String adminemail=jwtUtil.getEmailFromToken(token);
		 Optional<Muser>opmuser=muserrepositories.findByEmail(adminemail);
		 if(opmuser.isPresent()) {
			 Muser user= opmuser.get();
			 String role=user.getRole().getRoleName();
		 
		 if(role.equals("ADMIN")) {
			Optional<MuserApprovals> opapproval=MuserApproval.findById(id);
			if(opapproval.isPresent()) {
				
				MuserApproval.deleteById(id);
				return ResponseEntity.ok("user Rejected Successfully");
			}else {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
		 }else {

			  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		 }
		 }else {
			  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		 }
	}catch (Exception e) {
	    e.printStackTrace();
	    // Return an empty Page with a 200 OK status
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
public ResponseEntity<?>ApproveUser(HttpServletRequest request,Long id,String token){
	try{
		 String adminemail=jwtUtil.getEmailFromToken(token);
		 Optional<Muser>opmuser=muserrepositories.findByEmail(adminemail);
		 if(opmuser.isPresent()) {
			 Muser user= opmuser.get();
			 String role=user.getRole().getRoleName();
		 
		 if(role.equals("ADMIN")) {
			Optional<MuserApprovals> opapproval=MuserApproval.findById(id);
			if(opapproval.isPresent()) {
				MuserApprovals approval=opapproval.get();
				Muser muser=new Muser();
				muser.setUsername(approval.getUsername());
				muser.setEmail(approval.getEmail());
				muser.setPhone(approval.getPhone());
				muser.setDob(approval.getDob());
				muser.setInstitutionName(approval.getInstitutionName());
				muser.setProfile(approval.getProfile());
				muser.setPassword(approval.getPsw(),passwordEncoder);
				muser.setRole(approval.getRole());
				muser.setIsActive(true);
				muser.setInactiveDescription("");
				muser.setSkills(approval.getSkills());
				muser.setCountryCode(approval.getCountryCode());
				Muser usersaved =muserrepositories.save(muser);
				BatchenrollService.AssignDefaultBatchesPub( request,usersaved);
				List<String> bcc = null;
				List<String> cc = null;
				String institutionname = approval.getInstitutionName();
				 String domain = request.getHeader("origin"); // Extracts the domain dynamically

		          // Fallback if "Origin" header is not present (e.g., direct backend requests)
		          if (domain == null || domain.isEmpty()) {
		              domain = request.getScheme() + "://" + request.getServerName();
		              if (request.getServerPort() != 80 && request.getServerPort() != 443) {
		                  domain += ":" + request.getServerPort();
		              }
		          }

		          // Construct the Sign-in Link
		          String signInLink = domain + "/login";
				String body = String.format(
				    "<html>"
				        + "<body>"
				        + "<h2>Welcome to LearnHub  Portal!</h2>"
				        + "<p>Dear %s,</p>"
				        + "<p>We are thrilled to have you as a %s at LearnHub.</p>"
				        + "<p>Here are your login credentials:</p>"
				        + "<ul>"
				        + "<li><strong>Username (Email):</strong> %s</li>"
				        + "<li><strong>Password:</strong> %s</li>"
				        + "</ul>"
				        + "<p>If you need any assistance, our support team is here to help.</p>"
	                  + "<p>Click the link below to sign in:</p>"
	                  + "<p><a href='" + signInLink + "' style='font-size:16px; color:blue;'>Sign In</a></p>"
				        + "<p>Best Regards,<br>LearnHub Team</p>"
				        + "</body>"
				        + "</html>",
				        approval.getUsername(), // Trainer Name
				        approval.getRole().getRoleName(),
				    approval.getEmail(), // Trainer Username (email)
				    approval.getPsw() // Trainer Password
				);

				if (institutionname != null && !institutionname.isEmpty()) {
				    try {
				        List<String> emailList = new ArrayList<>();
				        emailList.add(approval.getEmail());
				        emailservice.sendHtmlEmailAsync(
				            institutionname, 
				            emailList,
				            cc, 
				            bcc, 
				            "Welcome to LearnHub -  Access Granted!", 
				            body
				        );
				    } catch (Exception e) {
				        logger.error("Error sending mail: " + e.getMessage());
				    }
				}

				MuserApproval.deleteById(id);
				return ResponseEntity.ok("user Approved Successfully");
			}else {
				 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			}
		 }else {

			  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		 }
		 }else {
			  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		 }
	}catch (Exception e) {
	    e.printStackTrace();
	    // Return an empty Page with a 200 OK status
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}

public ResponseEntity<Page<MuserDto>> searchTrainerByAdmin( String username, String email, String phone, LocalDate dob,
	       String skills, int page, int size,String token
	        ) {
		try{
			 String adminemail=jwtUtil.getEmailFromToken(token);
			 Optional<Muser>opmuser=muserrepositories.findByEmail(adminemail);
			 if(opmuser.isPresent()) {
				 Muser user= opmuser.get();
				 String role=user.getRole().getRoleName();
			 
			 if(role.equals("ADMIN")) {
				 String institutionName= user.getInstitutionName();
		    Pageable pageable = PageRequest.of(page, size);
		Page<MuserDto> Uniquestudents=muserPageRepo.CustomesearchForAdmin(username, email, phone,dob, institutionName, "TRAINER",skills, pageable);
		return ResponseEntity.ok(Uniquestudents);
			 }else {

				  return ResponseEntity.ok(Page.empty());
			 }
			 }else {
				  return ResponseEntity.ok(Page.empty());
			 }
		}catch (Exception e) {
		    e.printStackTrace();
		    logger.error("", e);
		    // Return an empty Page with a 200 OK status
		    return ResponseEntity.ok(Page.empty());
		}
	}

public ResponseEntity<Page<MuserDto>> searchDynamicUserByAdmin(
        String username, String email, String phone, LocalDate dob,
        String skills, String roleName, int page, int size, String token) {

    try {
        String adminEmail = jwtUtil.getEmailFromToken(token);
        Optional<Muser> opmuser = muserrepositories.findByEmail(adminEmail);

        if (opmuser.isPresent()) {
            Muser user = opmuser.get();
            String adminRole = user.getRole().getRoleName();

            if (adminRole.equals("ADMIN")) {
                String institutionName = user.getInstitutionName();
                Pageable pageable = PageRequest.of(page, size);

                // Search based on the roleName provided in the URL
                Page<MuserDto> dynamicUsers = muserPageRepo.searchByRoleAndFilters(username, email, phone, dob, institutionName, roleName, skills, pageable);
                return ResponseEntity.ok(dynamicUsers);

            } else {
                return ResponseEntity.ok(Page.empty()); // If the user is not an admin, return an empty page
            }

        } else {
            return ResponseEntity.ok(Page.empty()); // If the admin doesn't exist, return an empty page
        }

    } catch (Exception e) {
        e.printStackTrace();
        logger.error("", e);
        // Return an empty Page with a 200 OK status in case of error
        return ResponseEntity.ok(Page.empty());
    }
}


public ResponseEntity<Page<MuserDto>> searchUserByAdminorTrainer( String username, String email, String phone, LocalDate dob,
	       String skills, int page, int size,String token
	        ) {
		try{
			 String adminemail=jwtUtil.getEmailFromToken(token);
			 Optional<Muser>opmuser=muserrepositories.findByEmail(adminemail);
			 if(opmuser.isPresent()) {
				 Muser user= opmuser.get();
				 String role=user.getRole().getRoleName();
			 
			 if(role.equals("ADMIN")|| role.equals("TRAINER") ) {
				 String institutionName= user.getInstitutionName();
		    Pageable pageable = PageRequest.of(page, size);
		Page<MuserDto> Uniquestudents=muserPageRepo.CustomesearchForAdmin(username, email, phone,dob, institutionName, "USER",skills, pageable);
		return ResponseEntity.ok(Uniquestudents);
			 }else {

				  return ResponseEntity.ok(Page.empty());
			 }
			 }else {
				  return ResponseEntity.ok(Page.empty());
			 }
		}catch (Exception e) {
		    e.printStackTrace();
		    logger.error("", e);
		    // Return an empty Page with a 200 OK status
		    return ResponseEntity.ok(Page.empty());
		}
	}


public ResponseEntity<Page<MuserDto>> searchStudentsOfTrainer( String username, String email, String phone, LocalDate dob,
	       String skills, int page, int size,String token
	        ) {
		try{
			 String traineremail=jwtUtil.getEmailFromToken(token);
			 Optional<Muser>opmuser=muserrepositories.findByEmail(traineremail);
			 if(opmuser.isPresent()) {
				 Muser user= opmuser.get();
				 String role=user.getRole().getRoleName();
			 
			 if( role.equals("TRAINER") ) {
		    Pageable pageable = PageRequest.of(page, size);
		Page<MuserDto> Uniquestudents=muserPageRepo.searchStudentsOfTrainer(traineremail,username, email, phone,dob,skills, pageable);
		return ResponseEntity.ok(Uniquestudents);
			 }else {

				  return ResponseEntity.ok(Page.empty());
			 }
			 }else {
				  return ResponseEntity.ok(Page.empty());
			 }
		}catch (Exception e) {
		    e.printStackTrace();
		    logger.error("", e);
		    // Return an empty Page with a 200 OK status
		    return ResponseEntity.ok(Page.empty());
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public List<SearchDto> getBatchesOfUser(String token, String email) {
    try {
        // Fetch batches for the user
        return muserrepositories.getBatchOfUser(email);
        
    } catch (Exception e) {
        e.printStackTrace(); // Log the exception
        return Collections.emptyList(); // Return an empty list in case of an error
    }
}


public ResponseEntity<?> getRoleList(String token) {
    try {
        String role = jwtUtil.getRoleFromToken(token);
        if ("ADMIN".equals(role) || "TRAINER".equals(role)) {
           List<Map<String,Object>> roles= roleRepo.fetchAllRoles();
           
           return ResponseEntity.ok(roles);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    } catch (Exception e) {
        e.printStackTrace();
        logger.error("Error fetching users by role", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}



}