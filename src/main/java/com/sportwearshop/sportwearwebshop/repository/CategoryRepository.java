package com.sportwearshop.sportwearwebshop.repository;

import com.sportwearshop.sportwearwebshop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}