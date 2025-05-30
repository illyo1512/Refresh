package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}