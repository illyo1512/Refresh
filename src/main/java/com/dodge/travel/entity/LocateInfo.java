package com.dodge.travel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "locate_info")
public class LocateInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer infoId;

    private String placeName;
    private String placeLocation;
    private String placeDetail;
}