package com.chouket370.gestiontaches.dto;

import com.chouket370.gestiontaches.model.Priority;
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
public class updatedTaskDTO {
    private Optional<String> title = Optional.empty();
    private Optional<String> description = Optional.empty();
    private Optional<LocalDateTime> dueDate = Optional.empty();
    private Optional<Boolean> completed = Optional.empty();
    private Optional<String> assignedToUsername = Optional.empty();
    private Optional<Priority> priority= Optional.empty();
}
