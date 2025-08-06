package com.chouket370.gestiontaches.repository;


import com.chouket370.gestiontaches.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUserUsername(String username);
    Optional<Task> findByIdAndUserUsername(Long id, String username);
    List<Task> findByUserUsernameAndCompletedTrue(String username);
    List<Task> findByUserUsernameAndCompletedFalse(String username);

    List<Task> findByUserUsernameAndDueDateBetween(String username, LocalDateTime start, LocalDateTime end);
    List<Task> findByUserUsernameAndArchivedTrue(String username);
    List<Task> findByUserUsernameAndCompletedFalseAndArchivedFalseAndDueDateBetween(String username, LocalDateTime start, LocalDateTime end);




}
