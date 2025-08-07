package com.chouket370.gestiontaches.service;

import com.chouket370.gestiontaches.dto.TaskResponseDTO;
import com.chouket370.gestiontaches.dto.TaskRequestDTO;
import com.chouket370.gestiontaches.dto.updatedTaskDTO;
import com.chouket370.gestiontaches.model.Task;
import com.chouket370.gestiontaches.model.User;
import com.chouket370.gestiontaches.repository.TaskRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;

    public List<TaskResponseDTO> getUserTasks() {
        String username = currentUserService.getCurrentUsername();
        List<Task> tasks = taskRepository.findByUserUsername(username);
        return tasks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    public TaskResponseDTO getTaskById(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        return mapToResponseDTO(task);
    }
    public void deleteTask(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> getCompletedTasks() {
        String username = currentUserService.getCurrentUsername();
        List<Task> tasks = taskRepository.findByUserUsernameAndCompletedTrue(username);
        return tasks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> getTodayTasks() {
        String username = currentUserService.getCurrentUsername();
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        System.out.println("Username: " + username);
        System.out.println("Start of day: " + startOfDay);
        System.out.println("End of day: " + endOfDay);

        List<Task> tasks = taskRepository.findByUserUsernameAndDueDateBetween(username, startOfDay, endOfDay);

        System.out.println("Tasks found: " + tasks.size());

        return tasks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }


    public List<TaskResponseDTO> getArchivedTasks() {
        String username = currentUserService.getCurrentUsername();
        List<Task> tasks = taskRepository.findByUserUsernameAndArchivedTrue(username);
        return tasks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }





     public TaskResponseDTO updateTask(Long taskId, updatedTaskDTO updatedTaskDto) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));

        if (task.isArchived()) {
            throw new IllegalStateException("Archived tasks cannot be modified");
        }

        updatedTaskDto.getTitle().ifPresent(task::setTitle);
        updatedTaskDto.getDescription().ifPresent(task::setDescription);
        updatedTaskDto.getDueDate().ifPresent(task::setDueDate);
        updatedTaskDto.getCompleted().ifPresent(task::setCompleted);

        if (task.isCompleted() && task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            task.setArchived(true);
        }

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    public TaskResponseDTO createTask(TaskRequestDTO taskDto) {
        User user = currentUserService.getCurrentUser();

        Task task = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .completed(taskDto.isCompleted())
                .dueDate(taskDto.getDueDate())
                .archived(taskDto.isArchived())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    public void archiveCompletedOverdueTasks() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (!task.isArchived() && task.isCompleted()
                    && task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
                task.setArchived(true);
                taskRepository.save(task);
            }
        }
    }

    public void unarchiveTask(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        task.setArchived(false);
        taskRepository.save(task);
    }

    public void changeDueDate(Long taskId, LocalDateTime newDueDate) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        task.setDueDate(newDueDate);
        taskRepository.save(task);
    }

    public void archiveTask(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        task.setArchived(true);
        taskRepository.save(task);
    }
    public void completeTask(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        task.setCompleted(true);
        taskRepository.save(task);
    }
    public void uncompleteTask(Long taskId) {
        String username = currentUserService.getCurrentUsername();
        Task task = taskRepository.findByIdAndUserUsername(taskId, username)
                .orElseThrow(() -> new EntityNotFoundException("Task not found or access denied"));
        task.setCompleted(false);
        taskRepository.save(task);
    }



    private TaskResponseDTO mapToResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .completed(task.isCompleted())
                .archived(task.isArchived())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
    public List<TaskResponseDTO> getTasksDueSoon() {
        String username = currentUserService.getCurrentUsername();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusHours(24);

        List<Task> tasks = taskRepository.findByUserUsernameAndCompletedFalseAndArchivedFalseAndDueDateBetween(
                username, now, next24Hours);

        return tasks.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

}

