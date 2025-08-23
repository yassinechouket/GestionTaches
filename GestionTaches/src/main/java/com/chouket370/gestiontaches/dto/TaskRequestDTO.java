package com.chouket370.gestiontaches.dto;

import com.chouket370.gestiontaches.model.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Long assigneeId;
}
