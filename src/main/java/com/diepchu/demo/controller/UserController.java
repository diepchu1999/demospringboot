package com.diepchu.demo.controller;

import com.diepchu.demo.domain.User;
import com.diepchu.demo.domain.dto.ResultPaginationDTO;
import com.diepchu.demo.service.UserService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.diepchu.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
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
        return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Invalid ID");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("Deleted User");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User fetchUserById = userService.fetchUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUserById);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> specification, Pageable pageable) {
//        String currentPageString = currentPage.orElse("");
//        String pageSizeString = pageSize.orElse("");
//        int currentPageInt = Integer.parseInt(currentPageString);
//        int pageSizeInt = Integer.parseInt(pageSizeString);
//        Pageable pageable = PageRequest.of(currentPageInt - 1, pageSizeInt);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(specification, pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User currentUser = this.userService.fetchUserById(user.getId());
        return ResponseEntity.ok(currentUser);
    }


}
