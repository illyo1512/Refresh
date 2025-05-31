package com.refresh.refresh.repository;

import com.refresh.refresh.entity.SelfRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfRouteRepository extends JpaRepository<SelfRoute, Integer> {
}