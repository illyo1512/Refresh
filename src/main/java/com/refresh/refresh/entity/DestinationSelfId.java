package com.refresh.refresh.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class DestinationSelfId implements Serializable {
    private Integer destinationId;
    private Integer selfRouteId;

    // 기본 생성자
    public DestinationSelfId() {}

    // 두 값을 받는 생성자
    public DestinationSelfId(Integer destinationId, Integer selfRouteId) {
        this.destinationId = destinationId;
        this.selfRouteId = selfRouteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DestinationSelfId that = (DestinationSelfId) o;
        return Objects.equals(destinationId, that.destinationId) &&
               Objects.equals(selfRouteId, that.selfRouteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationId, selfRouteId);
    }
}
