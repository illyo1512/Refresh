package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}