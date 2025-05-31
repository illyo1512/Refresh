package com.refresh.refresh.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettingId implements Serializable {
    private Integer settingId;
    private Integer userId;
}
