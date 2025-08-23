package com.chouket370.gestiontaches.dto;

import com.chouket370.gestiontaches.model.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDTO {
    private Long taskId;
    private Long ownerId;
    private Long assignedId;
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime dueTime;
    private LocalDateTime lastUpdate;
}
