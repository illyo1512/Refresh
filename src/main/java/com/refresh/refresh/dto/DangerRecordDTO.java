package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class DangerRecordDTO {
    private Integer recordId;
    private Integer detailId;
    private String dangerJsonPath;
    private double minLan;
    private double minLat;
    private double maxLan;
    private double maxLat;
}