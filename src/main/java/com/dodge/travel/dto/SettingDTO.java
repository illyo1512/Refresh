package com.dodge.travel.dto;

import lombok.Data;

@Data
public class SettingDTO {
    private Integer settingId;
    private Integer userId;
    private Boolean nightMod;
    private Boolean colorBlind;
    private Integer alertLevel;
}