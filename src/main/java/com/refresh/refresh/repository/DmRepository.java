package com.refresh.refresh.repository;

import com.refresh.refresh.entity.Dm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmRepository extends JpaRepository<Dm, Integer> {
}