package com.diepchu.demo.service;

import com.diepchu.demo.domain.Company;
import com.diepchu.demo.domain.User;
import com.diepchu.demo.domain.response.*;
import com.diepchu.demo.repository.CompanyRepository;
import com.diepchu.demo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {
        if(user.getCompany() != null){
            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
            user.setCompany(companyOptional.orElse(companyOptional.get()));
        }
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User user) {
        User currentUser = this.fetchUserById(user.getId());
        if(currentUser != null){
            currentUser.setAddress(user.getAddress());
            currentUser.setGender(user.getGender());
            currentUser.setAge(user.getAge());
            currentUser.setName(user.getName());

            if (user.getCompany() != null) {
                Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
                user.setCompany(companyOptional.orElse(companyOptional.get()));
            }
        }
        return this.userRepository.save(currentUser);
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
                        item.getCreatedAt(),
                        new ResUserDTO.CompanyUser(item.getCompany() != null ? item.getCompany().getId() : 0,
                                item.getCompany() !=null ? item.getCompany().getName() : null)
                )).toList();
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber()+1)
                .pageSize(pageable.getPageSize())
                .total(users.getTotalElements())
                .pages(users.getTotalPages())
                .build();

        return ResultPaginationDTO.builder()
                .meta(meta)
                .result(listUser)
                .build();
    }


    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public  boolean isEmailExist(String email) {
        return  this.userRepository.existsUserByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user){
        ResCreateUserDTO.ResCreateUserDTOBuilder builder = ResCreateUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .gender(user.getGender())
                .address(user.getAddress());

        if (user.getCompany() != null) {
            ResCreateUserDTO.CompanyUser com = new ResCreateUserDTO.CompanyUser();
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            builder.company(com);
        }

        ResCreateUserDTO resCreateUserDTO = builder.build();
        return resCreateUserDTO;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user){
        ResUpdateUserDTO.ResUpdateUserDTOBuilder builder = ResUpdateUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .updatedAt(user.getUpdatedAt())
                .gender(user.getGender())
                .address(user.getAddress());
        if (user.getCompany() != null) {
            ResUpdateUserDTO.CompanyUser com = new ResUpdateUserDTO.CompanyUser();
            com.setId(user.getCompany().getId());
            com.setName(user.getCompany().getName());
            builder.company(com);
        }

        return builder.build();
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }

    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

}
