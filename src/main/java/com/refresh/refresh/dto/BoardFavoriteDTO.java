package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardFavoriteDTO {
    private Integer favoriteId; 
    private Integer userId;
    private Integer routeBoardId; 
    private LocalDateTime createdAt;
}
