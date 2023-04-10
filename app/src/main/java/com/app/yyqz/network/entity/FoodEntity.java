package com.app.yyqz.network.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodEntity implements Serializable {
    private int id;
    private String title;
    private String desc;
    private String imageName;
    private int type;
    private String aid;
    private int score;
}
