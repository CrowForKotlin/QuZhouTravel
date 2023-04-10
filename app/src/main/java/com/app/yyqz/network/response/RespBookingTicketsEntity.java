package com.app.yyqz.network.response;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBookingTicketsEntity {
    private int code;
    private ArrayList<UserTicketsDatas> userTicketsDatas;
}

