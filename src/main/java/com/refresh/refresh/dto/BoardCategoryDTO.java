package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class BoardCategoryDTO {
    private Integer categoryId;
    private String categoryDetail;
    private Integer upperCategoryId; // 추가된 필드
}