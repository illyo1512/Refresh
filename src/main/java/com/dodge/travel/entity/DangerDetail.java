package com.dodge.travel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "danger_detail")
public class DangerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailId;

    private String dangerType;
    private String dangerDetail;
    private String dangerCountermeasure;
}