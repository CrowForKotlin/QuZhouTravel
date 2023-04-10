package com.app.yyqz.network;

import android.app.Activity;

import com.app.yyqz.utils.OkHttpUtil;
import com.app.yyqz.utils.TipsUtil;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class BaiduFactory {

    public static void GetPlace(Activity activity, String query, String region, NetWorkFactory.IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.Baidu_Search_Place).newBuilder().addQueryParameter("query", query).addQueryParameter("region", region).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    public static void GetTrafficScope(Activity activity, String lat, String lng, NetWorkFactory.IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.Baidu_Traffic_Scope).newBuilder().addQueryParameter("center", lat+","+lng).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

}
