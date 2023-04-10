package com.app.yyqz.network;

import android.app.Activity;

import com.app.yyqz.utils.OkHttpUtil;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.ResponseBody;


public class NetWorkFactory {

    //  统一的网络请求回调接口
    @FunctionalInterface
    public interface IReqCallback {
        void onSuccess(String response);
    }

    //  统一的网络请求回调接口
    @FunctionalInterface
    public interface IReqDownloadCallback {
        void onSuccess(ResponseBody body);
    }

    // 登录
    public static void Login(Activity activity, String usr, String pwd, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("password", pwd).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.Login).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 注册
    public static void Reg(Activity activity, String name, String usr, String pwd, IReqCallback iReqCallback) {

        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("name", name).add("username", usr).add("password", pwd).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.Reg).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 获取主页数据
    public static void GetHomePage(Activity activity, IReqCallback iReqCallback) {
        // 构建 一个请求
        Request request = new Request.Builder().get().url(NetworkPath.HomePage).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 门票预定
    public static void BookTickets(Activity activity, String usr, String ticketsCount, String tourName, String tourImageName, String date, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("tickets_count", ticketsCount).add("tour_name", tourName).add("tour_imagename", tourImageName).add("date", date).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.BookTickets).build();


        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 获取预定的门票
    public static void GetPreBookingTickets(Activity activity, String usr, IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.UserBookTickets).newBuilder().addQueryParameter("username", usr).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 取消门票预定
    public static void CancelBookingTickets(Activity activity, String usr, String tourName, String ticketsCount, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("tour_name", tourName).add("tickets_count", ticketsCount).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.CancelUserBookTickets).build();


        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 修改用户名
    public static void ChangeUserName(Activity activity, String usr, String name, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("name", name).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.ChangeUsrName).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 修改用户密码
    public static void ChangeUserPwd(Activity activity, String usr, String newPwd, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("new_pwd", newPwd).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.ChangeUsrPwd).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 修改喜欢的类型
    public static void ChangeLikeType(Activity activity, String usr, String likeType, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("like_type", likeType).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.ChangeLikeType).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 获取美食主页 -- 推荐
    public static void getFoodHomePageByRecommend(Activity activity, String usr, IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.FoodHomePage).newBuilder().addQueryParameter("username", usr).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 获取美食主页 -- 根据用户的喜欢类型
    public static void getFoodHomePageByLikeType(Activity activity, String usr, IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.FoodHomePageLikeType).newBuilder().addQueryParameter("username", usr).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    // 修改用户评分
    public static void ChangeUserScore(Activity activity, String usr, String foodTitle, int foodScore, IReqCallback iReqCallback) {
        // 构建 请求的参数
        FormBody body = new FormBody.Builder().add("username", usr).add("food_title", foodTitle).add("food_score", String.valueOf(foodScore)).build();

        // 构建 一个请求
        Request request = new Request.Builder().post(body).url(NetworkPath.ChangeUsrScore).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    public static void DownloadMp3(Activity activity, String fileName, IReqDownloadCallback iReqCallback) {

        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.DownloadMp3).newBuilder().addQueryParameter("file_name", fileName).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(NetworkPath.DownloadMp3 + "?file_name=" + fileName).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    public static void StartTour(Activity activity, String usr, String tourName, Boolean isStart, IReqCallback iReqCallback) {
        FormBody body = new FormBody.Builder().add("username", usr).add("tour_name", tourName).add("tour_start", isStart.toString()).build();

        Request request = new Request.Builder().post(body).url(NetworkPath.StartTour).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    public static void GetTourIsStart(Activity activity, String usr, String tourName, IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.GetTourIsStart).newBuilder().addQueryParameter("username", usr).addQueryParameter("tour_name", tourName).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }

    public static void GetTourStart(Activity activity, String usr, IReqCallback iReqCallback) {
        // 构建 请求的参数
        HttpUrl url = HttpUrl.get(NetworkPath.GetTourStart).newBuilder().addQueryParameter("username", usr).build();

        // 构建 一个请求
        Request request = new Request.Builder().url(url).build();

        OkHttpUtil.doExecute(activity, request, iReqCallback);
    }


}