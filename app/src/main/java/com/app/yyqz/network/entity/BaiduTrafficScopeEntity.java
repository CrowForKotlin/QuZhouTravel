package com.app.yyqz.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaiduTrafficScopeEntity {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("description")
    private String description;
    @SerializedName("evaluation")
    private EvaluationDTO evaluation;
    @SerializedName("road_traffic")
    private List<RoadTrafficDTO> roadTraffic;

    @NoArgsConstructor
    @Data
    public static class EvaluationDTO {
        @SerializedName("status")
        private Integer status;
        @SerializedName("status_desc")
        private String statusDesc;
    }

    @NoArgsConstructor
    @Data
    public static class RoadTrafficDTO {
        @SerializedName("road_name")
        private String roadName;
    }
}
