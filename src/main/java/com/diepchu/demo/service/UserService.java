package com.diepchu.demo.service;

import com.diepchu.demo.domain.User;
import com.diepchu.demo.domain.dto.*;
import com.diepchu.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        return user.orElse(null);
    }

    public ResultPaginationDTO fetchAllUsers(Specification<User> specification, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(specification,pageable);
        List<ResUserDTO> listUser = users.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getUpdatedAt(),
                        item.getCreatedAt()
                )).toList();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber()+1)
                .pageSize(pageable.getPageSize())
                .total(users.getTotalElements())
                .pages(users.getTotalPages())
                .build();

        return ResultPaginationDTO.builder()
                .meta(meta)
                .data(listUser)
                .build();
    }

    public User updateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());

        if (currentUser != null) {
            currentUser.setName(user.getName());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public  boolean isEmailExist(String email) {
        return  this.userRepository.existsUserByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO resCreateUserDTO = ResCreateUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();
        return resCreateUserDTO;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO resUpdateUserDTO = ResUpdateUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .updatedAt(user.getUpdatedAt())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();
        return resUpdateUserDTO;
    }


    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }

    }
}
