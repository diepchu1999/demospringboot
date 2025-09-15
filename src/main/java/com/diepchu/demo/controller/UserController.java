package com.diepchu.demo.controller;

import com.diepchu.demo.domain.User;
import com.diepchu.demo.service.UserService;
import com.diepchu.demo.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User createUser = this.userService.handleCreateUser(user);
        return  ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @DeleteMapping("/users/{id}")
    public  ResponseEntity<String>  deleteUser(@PathVariable long id) throws IdInvalidException {
        if (id>=1500){
            throw new IdInvalidException("Invalid ID");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("Deleted User");
    }

    @GetMapping("/users/{id}")
    public  ResponseEntity<User> getUserById(@PathVariable long id) {
        User fetchUserById = userService.fetchUserById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(fetchUserById);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>>  getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers());
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser( @RequestBody User user) {
        User currentUser = this.userService.fetchUserById(user.getId());
        return ResponseEntity.ok(currentUser);
    }




}
