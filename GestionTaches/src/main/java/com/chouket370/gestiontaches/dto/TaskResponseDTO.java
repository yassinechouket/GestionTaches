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
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;
    private boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String assignedToUsername;
    private Priority priority;

}
