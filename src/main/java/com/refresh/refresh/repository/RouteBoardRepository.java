package com.refresh.refresh.repository;

import com.refresh.refresh.entity.RouteBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteBoardRepository extends JpaRepository<RouteBoard, Integer> {
}