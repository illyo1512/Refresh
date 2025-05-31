package com.refresh.refresh.repository;

import com.refresh.refresh.entity.DestinationSelf;
import com.refresh.refresh.entity.DestinationSelfId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationSelfRepository extends JpaRepository<DestinationSelf, DestinationSelfId> {
}