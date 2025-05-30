package com.refresh.refresh.dto;

import lombok.Data;

@Data
public class DestinationSaveDTO {
    private Integer destinationId;
    private Integer savedRouteId;
    private Integer infoId;
    private String destinationLocate;
    private Integer destinationOrder;
}