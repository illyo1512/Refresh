package com.refresh.refresh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReportResultId implements Serializable {
    private Integer resultId;
    private Integer reportId;
}
