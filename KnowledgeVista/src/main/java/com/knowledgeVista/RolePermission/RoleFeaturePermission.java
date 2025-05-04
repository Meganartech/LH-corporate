package com.knowledgeVista.RolePermission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "role_feature_permissions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roleId"}) // Ensures roleId is unique in this table
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleFeaturePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roleId; // Assuming roles come from another service or table

    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private Feature feature;

    @ManyToOne
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;
}
