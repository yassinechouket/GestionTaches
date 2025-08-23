package com.chouket370.gestiontaches.service;

import com.chouket370.gestiontaches.dto.TaskResponseDTO;
import com.chouket370.gestiontaches.dto.TaskRequestDTO;
import com.chouket370.gestiontaches.dto.updatedTaskDTO;
import com.chouket370.gestiontaches.model.Priority;
import com.chouket370.gestiontaches.model.Task;
import com.chouket370.gestiontaches.model.User;
import com.chouket370.gestiontaches.repository.TaskRepository;

import com.chouket370.gestiontaches.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;

    public List<TaskResponseDTO> getUserTasks() {
        User currentUser = currentUserService.getCurrentUser();
        List<Task> tasks = taskRepository.findAll().stream()
                .filter(t ->
                        (t.getAssignedTo() != null && t.getAssignedTo().getId().equals(currentUser.getId())) ||
                                (t.getUser().getId().equals(currentUser.getId())) ||
                                (t.getAssignedTo() == null)
                )
                .toList();

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
        updatedTaskDto.getPriority().ifPresent(task::setPriority);


        updatedTaskDto.getAssignedToUsername().ifPresent(assignedUsername -> {
            User assignedUser = userRepository.findByUsername(assignedUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("Assigned user not found: " + assignedUsername));
            task.setAssignedTo(assignedUser);
        });

        if (task.isCompleted() && task.getDueDate() != null && task.getDueDate().isBefore(LocalDateTime.now())) {
            task.setArchived(true);
        }

        Task savedTask = taskRepository.save(task);
        return mapToResponseDTO(savedTask);
    }

    public TaskResponseDTO createTask(TaskRequestDTO taskDto) {
        User owner = currentUserService.getCurrentUser();

        Task.TaskBuilder taskBuilder = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .priority(taskDto.getPriority())
                .dueDate(taskDto.getDueDate())
                .user(owner);

        if (taskDto.getAssigneeId() != null) {
            User assignee = userRepository.findById(taskDto.getAssigneeId())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + taskDto.getAssigneeId()));
            taskBuilder.assignedTo(assignee);
        }

        Task savedTask = taskRepository.save(taskBuilder.build());
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
                .taskId(task.getId())
                .ownerId(task.getUser().getId())
                .assignedId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .dueTime(task.getDueDate())
                .lastUpdate(task.getUpdatedAt())
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
