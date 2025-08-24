package com.chouket370.gestiontaches.controller;




import com.chouket370.gestiontaches.dto.UserDTO;
import com.chouket370.gestiontaches.dto.UserBasicDTO;
import com.chouket370.gestiontaches.model.User;
import com.chouket370.gestiontaches.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserService userService;



    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toUserDTO)
                .toList();
    }
    @GetMapping("/{id}")
    public Optional<UserDTO> getUserById(@PathVariable Long id) {
        return Optional.of(toUserDTO(userService.getUserById(id)));
    }
    @GetMapping("/name/{name}")
    public Optional<UserDTO> getUserByUsername(@PathVariable String name) {
        return Optional.of(toUserDTO(userService.findByUsername(name)));
    }
    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
    @PostMapping("/save")
    public User saveUser(@RequestBody @Valid User user)
    {
        return userService.save(user);
    }
    private UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    @GetMapping("/basic")
    public List<UserBasicDTO> getAllUsernamesAndEmails() {
        return userService.getAllUsers().stream()
                .map(user -> new UserBasicDTO(user.getUsername(), user.getEmail()))
                .toList();
    }


}
