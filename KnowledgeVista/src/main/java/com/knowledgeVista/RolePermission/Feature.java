package com.knowledgeVista.RolePermission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "feature")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feature {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @Column(nullable = false)
    private String featureName;

    @Column(nullable = false)
    private boolean isActive;

}
