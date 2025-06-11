package com.knowledgeVista.Batch.Enrollment;

import java.time.LocalDateTime;


import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.User.Muser;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
	    uniqueConstraints = {
	        @UniqueConstraint(columnNames = {"user_id", "batch_id"})
	    }
	)
public class AccessRequest {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	  @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private Muser user;

	    @ManyToOne
	    @JoinColumn(name = "batch_id", nullable = false)
	    private Batch batch;

	    private LocalDateTime requestTime;

	    @Enumerated(EnumType.STRING)
	    private RequestStatus status; // PENDING, APPROVED, REJECTED
	    public  enum RequestStatus {
	        PENDING,
	        APPROVED,
	        REJECTED
	    }
	     
	    
	}

 


