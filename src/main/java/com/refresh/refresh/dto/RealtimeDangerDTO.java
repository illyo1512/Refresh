package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class RealtimeDangerDTO {
    private Integer dangerId;
    private String locateName;
    private Integer dangerDetailId;
    private String placeLocation;
    private String occurredAt;
}