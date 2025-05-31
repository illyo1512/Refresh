package com.refresh.refresh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(SettingId.class) // 다중 키를 주요 키로 사용
@Table(name = "setting")
public class Setting {
    @Id
    private Integer settingId;

    @Id
    private Integer userId;

    private Boolean nightMod;
    private Boolean colorBlind;
    private Integer alertLevel;
}