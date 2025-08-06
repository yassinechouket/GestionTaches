package com.chouket370.gestiontaches.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class updatedTaskDTO {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private boolean completed;

}
