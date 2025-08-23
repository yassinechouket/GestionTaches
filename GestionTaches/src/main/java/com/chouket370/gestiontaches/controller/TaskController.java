package com.chouket370.gestiontaches.controller;

import com.chouket370.gestiontaches.dto.TaskResponseDTO;
import com.chouket370.gestiontaches.dto.TaskRequestDTO;
import com.chouket370.gestiontaches.dto.updatedTaskDTO;
import com.chouket370.gestiontaches.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;


    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getUserTasks() {
        return ResponseEntity.ok(taskService.getUserTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }
    @GetMapping("/completed")
    public ResponseEntity<List<TaskResponseDTO>> getCompletedTasks() {
        return ResponseEntity.ok(taskService.getCompletedTasks());
    }
    @GetMapping("/today")
    public ResponseEntity<List<TaskResponseDTO>> getTodayTasks() {
        return ResponseEntity.ok(taskService.getTodayTasks());
    }

    @GetMapping("/archived")
    public ResponseEntity<List<TaskResponseDTO>> getArchivedTasks() {
        return ResponseEntity.ok(taskService.getArchivedTasks());
    }
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody updatedTaskDTO dto) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        return ResponseEntity.ok(Map.of("success", deleted ? "true" : "false"));
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archiveTask(@PathVariable Long id) {
        taskService.archiveTask(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveTask(@PathVariable Long id) {
        taskService.unarchiveTask(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/duedate")
    public ResponseEntity<Void> changeDueDate(
            @PathVariable Long id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate
    ) {
        taskService.changeDueDate(id, dueDate);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/auto-archive")
    public ResponseEntity<Void> autoArchiveCompletedOverdueTasks() {
        taskService.archiveCompletedOverdueTasks();
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/due-soon")
    public ResponseEntity<List<TaskResponseDTO>> getTasksDueSoon() {
        List<TaskResponseDTO> tasksDueSoon = taskService.getTasksDueSoon();
        return ResponseEntity.ok(tasksDueSoon);
    }
    @PutMapping("/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId) {
        taskService.completeTask(taskId);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{taskId}/uncomplete")
    public ResponseEntity<Void> uncompleteTask(@PathVariable Long taskId) {
        taskService.uncompleteTask(taskId);
        return ResponseEntity.ok().build();
    }

}
