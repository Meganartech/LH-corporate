package com.knowledgeVista.Batch.Enrollment;


import java.time.LocalDateTime;

import com.knowledgeVista.Batch.Enrollment.AccessRequest.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class AccessRequestDTO {
    private Long approvalId;
    private Long userId;
    private String username;
    private Long batchId;
    private String batchName;
    private LocalDateTime requestTime;
    private RequestStatus requestStatus;
}

