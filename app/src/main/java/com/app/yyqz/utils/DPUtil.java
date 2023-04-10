package com.app.yyqz.utils;

// 工具类 提供px和dip的相互转化

public class DPUtil {
    public static int dip2px(android.content.Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int px2dip(android.content.Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dp2px(android.content.Context context, int dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5);
    }

    public static int px2dp(android.content.Context context, int pxValue) {
        return (int) ((double)pxValue / context.getResources().getDisplayMetrics().density + 0.5);
    }
}

