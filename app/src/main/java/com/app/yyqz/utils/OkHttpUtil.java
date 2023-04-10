package com.app.yyqz.utils;

import static com.app.yyqz.utils.TipsUtil.showToast;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.entity.User;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Okhttp 工具类
public class OkHttpUtil {

    // 异步请求登录 避免阻塞 主线程导致UI卡顿
    public static void doExecute(Activity activity, Request request, NetWorkFactory.IReqCallback iReqCallback) {

        // 异步请求登录 避免阻塞 主线程导致UI卡顿
        OkHttpUtil.getInstance().newCall(request).enqueue(new Callback() {

            // 请求失败的回调
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(() -> showToast(activity.getApplicationContext(), "请求异常，请重试！"));
            }

            // 请求成功的回调
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                AtomicReference<String> data = new AtomicReference<>();
                try {
                    data.set(Objects.requireNonNull(response.body()).string());

                    //在主线程中执行回调
                    activity.runOnUiThread(() -> iReqCallback.onSuccess(data.get()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 异步请求登录 避免阻塞 主线程导致UI卡顿
    public static void doExecute(Activity activity, Request request, NetWorkFactory.IReqDownloadCallback iReqCallback) {
        // 异步请求登录 避免阻塞 主线程导致UI卡顿
        OkHttpUtil.getInstance().newCall(request).enqueue(new Callback() {

            // 请求失败的回调
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                activity.runOnUiThread(() -> showToast(activity.getApplicationContext(), "请求异常，请重试！"));
            }

            // 请求成功的回调
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                iReqCallback.onSuccess(response.body());
            }
        });
    }

    // 无法构造
    private OkHttpUtil() {
    }

    // 获取OkhhtpClient 单例 : 网络请求类一般封装成单例减少额外创建的开销
    public static OkHttpClient getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private final OkHttpClient singleton;

        Singleton() {
            singleton = new OkHttpClient.Builder()
                    .connectTimeout(20L, TimeUnit.SECONDS)
                    .readTimeout(20L, TimeUnit.SECONDS)
                    .writeTimeout(20L, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool(50, 60, TimeUnit.SECONDS))
                    .build();
        }

        public OkHttpClient getInstance() {
            return singleton;
        }
    }


}
