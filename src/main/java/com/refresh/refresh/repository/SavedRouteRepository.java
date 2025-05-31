package com.refresh.refresh.repository;

import com.refresh.refresh.entity.SavedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRouteRepository extends JpaRepository<SavedRoute, Integer> {
}