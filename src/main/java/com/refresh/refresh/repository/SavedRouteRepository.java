package com.refresh.refresh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.refresh.refresh.entity.SavedRoute;

public interface SavedRouteRepository extends JpaRepository<SavedRoute, Integer> {
}