package com.app.yyqz.network;


import java.net.URL;

public class NetworkPath {

    private static final String URL = "http://120.48.124.38:8777";
    private static final String BaiduAPI = "https://api.map.baidu.com";
    private static final String BaiduAPI_AK = "zTQG8dFSU6L0QtECKpfM7CvD92I8jKjz";
    public static final String BiliBili = "https://player.bilibili.com/player.html?cid=145147963&page=1&as_wide=1&high_quality=1&danmaku=0&aid=";
    public static String Login = URL + "/login";
    public static String Reg = URL + "/reg";
    public static String HomePage = URL + "/homepage";
    public static String FoodHomePage = URL + "/food/homepage";
    public static String FoodHomePageLikeType = URL + "/food/homepage/liketype";
    public static String BookTickets = URL + "/book/tickets";

    public static String StartTour = URL + "/tour/start";

    public static String GetTourIsStart = URL + "/tour/isstart";
    public static String GetTourStart = URL + "/tour/start/exists";
    public static String UserBookTickets = URL + "/book/tickets/user";
    public static String CancelUserBookTickets = URL + "/book/tickets/cancel";
    public static String ChangeUsrName = URL + "/user/change/name";
    public static String ChangeUsrPwd = URL + "/user/change/pwd";
    public static String ChangeLikeType = URL + "/user/change/liketype";
    public static String ChangeUsrScore = URL + "/user/change/score";
    public static String ImageContainer = URL + "/images/";
    public static String FoodImageContainer = URL + "/foods/images/";
    public static String DownloadMp3 = URL + "/download";
    public static String Baidu_Search_Place = BaiduAPI + "/place/v2/suggestion?output=json&city_limit=true&ak=" + BaiduAPI_AK;
    public static String Baidu_Traffic_Scope = BaiduAPI + "/traffic/v1/around?radius=2000&coord_type_input=gcj02&coord_type_output=bd09ll&ak=" + BaiduAPI_AK;
    public static String Baidu_Map = BaiduAPI + "/place/detail?output=html&src=webapp.baidu.openAP&uid=";

}