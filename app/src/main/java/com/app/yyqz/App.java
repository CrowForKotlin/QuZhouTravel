package com.app.yyqz;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.app.yyqz.utils.TipsUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class App extends Application {

    // 全局context
    private static Context context;

    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        // 捕获全局异常，方便查看错误日志
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            TipsUtil.printError(sw.toString());
        });
    }

    public static Context getContextObject() {
        return context;
    }


}
