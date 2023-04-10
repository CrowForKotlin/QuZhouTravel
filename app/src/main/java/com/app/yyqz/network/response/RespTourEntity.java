package com.app.yyqz.network.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespTourEntity implements Serializable {
    private float code;
    private String name;
    private String tickets;
    private String level;
    private String city;
    private String locate;
    private String openTime;
    private String desc;
    private String imageName;
}
