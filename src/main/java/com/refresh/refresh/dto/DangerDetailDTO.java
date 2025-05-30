package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class DangerDetailDTO {
    private Integer detailId;
    private String dangerType;
    private String dangerDetail;
    private String dangerCountermeasure;
}