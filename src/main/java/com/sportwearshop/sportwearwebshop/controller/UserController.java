package com.sportwearshop.sportwearwebshop.controller;

import com.sportwearshop.sportwearwebshop.entity.User;
import com.sportwearshop.sportwearwebshop.entity.User;
import com.sportwearshop.sportwearwebshop.repository.UserRepository;
import com.sportwearshop.sportwearwebshop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) { this.userRepository = userRepository; }

    @GetMapping
    public List<User> getAllUsers() { return userRepository.findAll(); }

    @PostMapping
    public User addUser(@RequestBody User user) { return userRepository.save(user); }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("No user with this id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}

