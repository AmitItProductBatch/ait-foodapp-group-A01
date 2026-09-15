package com.ait.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ait.app.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
