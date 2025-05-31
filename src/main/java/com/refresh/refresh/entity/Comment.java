package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer routeBoardId;

    private String content;
    private LocalDateTime createdAt;
}