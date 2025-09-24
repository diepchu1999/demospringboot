package com.diepchu.demo.controller;

import com.diepchu.demo.domain.User;
import com.diepchu.demo.domain.response.ResCreateUserDTO;
import com.diepchu.demo.domain.response.ResUpdateUserDTO;
import com.diepchu.demo.domain.response.ResultPaginationDTO;
import com.diepchu.demo.service.UserService;
import com.diepchu.demo.util.anotation.ApiMessage;
import com.diepchu.demo.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User user) throws  IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(user.getEmail());
        if(isEmailExist){
            throw new IdInvalidException("Email " + user.getEmail() + " đã tồn tại, vui lòng sử dụng email khác");
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User createUser = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(createUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<String> deleteUser(@PathVariable long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if(currentUser == null){
            throw new IdInvalidException("User với id = "+ id + " không tồn tại");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok("Deleted User");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResCreateUserDTO> getUserById(@PathVariable long id) throws IdInvalidException {
        User fetchUserById = userService.fetchUserById(id);
        if(fetchUserById == null){
            throw new IdInvalidException("User voi id = " +id + " khong ton tai");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResCreateUserDTO(fetchUserById));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all user")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> specification, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(specification, pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User currentUser = this.userService.handleUpdateUser(user);
        if(currentUser == null){
            throw new IdInvalidException("User voi id = " +user.getId() + " khong ton tai");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(currentUser));
    }



}
