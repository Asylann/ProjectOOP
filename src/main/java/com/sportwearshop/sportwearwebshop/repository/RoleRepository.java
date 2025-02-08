package com.sportwearshop.sportwearwebshop.repository;

import com.sportwearshop.sportwearwebshop.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
