package com.Ekatalog.repository;

import com.Ekatalog.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel , Long> {
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUsername(String username);

}