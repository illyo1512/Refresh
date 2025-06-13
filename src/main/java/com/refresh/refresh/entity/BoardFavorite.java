package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "board_favorite")
public class BoardFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer favoriteId;
    
    @Column(nullable = false)
    private Integer userId;

    @Column(name = "route_board_id", nullable = false)  // 컬럼명 명시
    private Integer routeBoardId;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private java.time.LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_board_id", insertable = false, updatable = false)
    private RouteBoard routeBoard;
}
