package com.knowledgeVista.Batch.Enrollment;

import com.knowledgeVista.Batch.Batch;
import com.knowledgeVista.User.MuserRoles;

import jakarta.persistence.Entity;
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
@Table(
    name = "batch_role_mapping",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_id", "batch_id"})
    }
)
@Getter
@Setter
public class BatchRoleMapping {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private MuserRoles role;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batch batch;

    public BatchRoleMapping() {}

    public BatchRoleMapping(MuserRoles role, Batch batch) {
        this.role = role;
        this.batch = batch;
    }
}

