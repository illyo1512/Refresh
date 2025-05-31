package com.refresh.refresh.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RealtimeDangerDTO {
    private Integer dangerId;
    private String locateName;
    private Integer dangerDetailId;
    private String placeLocation;
    private LocalDateTime occurredAt;
}