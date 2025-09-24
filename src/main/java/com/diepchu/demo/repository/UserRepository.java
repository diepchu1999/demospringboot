package com.diepchu.demo.repository;

import com.diepchu.demo.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.diepchu.demo.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    boolean existsUserByEmail(String email);

    User findByRefreshTokenAndEmail(String refreshToken, String email);

    List<User> findByCompany(Company company);
}
