package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.BoardCategory;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Integer> {
}