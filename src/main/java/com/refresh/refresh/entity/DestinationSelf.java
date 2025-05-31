package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(DestinationSelfId.class) // 다중 키를 주요 키로 사용
@Table(name = "destination_self")
public class DestinationSelf {
    @Id
    private Integer destinationId;

    @Id
    private Integer selfRouteId;

    @Column(nullable = false)
    private Integer infoId;

    private String destinationLocate;
    private Integer destinationOrder;
}