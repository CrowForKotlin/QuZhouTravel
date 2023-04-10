package com.app.yyqz.network.response;


import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespLoginEntity implements Serializable {
    int code;
    private String name;
    private String username;
    private String password;
    private String likeType;
}
