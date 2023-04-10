package com.app.yyqz.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

// 提示工具类
public class TipsUtil {

    private static Toast toast = null;
    private static final String LOGGER_TAG = "LOGGER_YYQZ";

    // 显示 Toast
    public static void showToast(Context context, String content) {

        // 如果toast != null 代表toast已经显示过了 所以 尝试取消显示Toast
        if (toast != null) toast.cancel();

        // 重新赋值后显示
        toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        toast.show();
    }

    // 内容描述同上
    public static void showLongToast(Context context, String content) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        toast.show();
    }

    // 打印错误
    public static void printError(String msg) {
        Log.e(LOGGER_TAG, msg);
    }
}
