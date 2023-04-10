package com.app.yyqz.network.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketsDatas {
    private int bookingTicketsCount;
    private String tourName;
    private String tourImageName;
    private String date;
}
