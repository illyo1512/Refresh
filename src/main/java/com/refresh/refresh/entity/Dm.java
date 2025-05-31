package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "dm")
public class Dm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dmId;

    @Column(nullable = false)
    private Integer userId;

    private String content;
    private String sender;
    private LocalDateTime sentAt;
}