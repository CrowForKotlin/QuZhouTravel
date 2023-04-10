package com.app.yyqz.network.response;

import com.app.yyqz.network.entity.FoodEntity;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespFoodEntity {
    private int code;
    private ArrayList<FoodEntity> foodEntities;
}
