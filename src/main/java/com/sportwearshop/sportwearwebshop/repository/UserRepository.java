package com.sportwearshop.sportwearwebshop.repository;

import com.sportwearshop.sportwearwebshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<Object> findByEmail(String email);
}
