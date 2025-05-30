package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class DangerRecordDTO {
    private Integer recordId;
    private Integer detailId;
    private String dangerLocation;
    private Integer dangerRadius;
    private Integer dangerLevel;
}