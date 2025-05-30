package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer commentId;
    private Integer userId;
    private Integer routeBoardId;
    private String content;
    private String createdAt;
}