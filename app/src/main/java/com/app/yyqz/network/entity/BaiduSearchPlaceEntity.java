package com.app.yyqz.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BaiduSearchPlaceEntity {
    @SerializedName("status")
    private Integer status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private List<ResultDTO> result;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @SerializedName("name")
        private String name;
        @SerializedName("location")
        private LocationDTO location;
        @SerializedName("uid")
        private String uid;
        @SerializedName("province")
        private String province;
        @SerializedName("city")
        private String city;
        @SerializedName("district")
        private String district;
        @SerializedName("business")
        private String business;
        @SerializedName("cityid")
        private String cityid;
        @SerializedName("tag")
        private String tag;
        @SerializedName("address")
        private String address;
        @SerializedName("children")
        private List<ChildrenDTO> children;
        @SerializedName("adcode")
        private String adcode;

        @NoArgsConstructor
        @Data
        public static class LocationDTO {
            @SerializedName("lat")
            private Double lat;
            @SerializedName("lng")
            private Double lng;
        }

        @NoArgsConstructor
        @Data
        public static class ChildrenDTO {
            @SerializedName("uid")
            private String uid;
            @SerializedName("name")
            private String name;
            @SerializedName("show_name")
            private String showName;
        }
    }
}