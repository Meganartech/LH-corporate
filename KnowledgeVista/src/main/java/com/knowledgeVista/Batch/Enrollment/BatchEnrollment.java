package com.knowledgeVista.Batch.Enrollment;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.User.Muser;

@Entity
@Getter
@Setter
public class BatchEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private Muser user;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    private LocalDateTime enrollmentDate;
    private LocalDateTime expiryDate;


    public BatchEnrollment() {}

    public BatchEnrollment(Muser user, Batch batch, Long durationHours) {
        this.user = user;
        this.batch = batch;
        this.enrollmentDate = LocalDateTime.now();
        this.expiryDate = this.enrollmentDate.plusHours(durationHours);
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(this.expiryDate);
    }

}

