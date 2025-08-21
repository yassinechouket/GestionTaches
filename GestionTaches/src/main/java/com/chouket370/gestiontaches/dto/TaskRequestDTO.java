package com.chouket370.gestiontaches.dto;

import com.chouket370.gestiontaches.model.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {
    private String title;
    private String description;
    @FutureOrPresent(message = "Due date must be in the future")
    private LocalDateTime dueDate;
    private boolean completed;
    private boolean archived;
    private String assignedToUsername;
    private Priority priority;
}
