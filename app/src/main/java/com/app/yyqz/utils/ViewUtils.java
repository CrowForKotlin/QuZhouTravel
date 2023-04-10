package com.app.yyqz.utils;

import android.view.MenuItem;
import android.view.View;

public class ViewUtils {

    public static long GAP_TIME = 1_000L;       // 1s 1000ms
    public static long GAP_TIME_SHORT = 250L;   // 0.25s 250ms



    // 显式声明是一个函数式接口
    @FunctionalInterface
    public interface IMenuClick {
        void onClick(MenuItem menuItem);
    }

    // 显式声明是一个函数式接口
    @FunctionalInterface
    public interface IViewClick {
        void onClick(View view);
    }

    // 扩展 View的点击事件，避免多次点击
    public static void clickGap(View view, Long flagTime, IViewClick iViewClick) {

        // 设置点击事件
        view.setOnClickListener(new View.OnClickListener() {

            // 记录点击后的时间
            long clickTime = 0;

            @Override
            public void onClick(View v) {

                // 获取当前时间
                long currentTime = System.currentTimeMillis();

                // 间隔时间 = 当前时间 - 标志时间
                // 如果间隔时间 大于上一次点击的时间 代表已经过了标志时间{flagTime}了-->然后执行回调
                if (currentTime - flagTime > clickTime) {

                    // 这次的点击时间 = 当前点击时间
                    clickTime = currentTime;

                    // 执行回调
                    iViewClick.onClick(view);
                }
            }
        });
    }

    public static void menuClickGap(MenuItem menuItem, Long flagTime, IMenuClick iMenuClick) {
        // 设置点击事件
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            // 记录点击后的时间
            long clickTime = 0L;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 获取当前时间
                long currentTime = System.currentTimeMillis();

                // 间隔时间 = 当前时间 - 标志时间
                // 如果间隔时间 大于上一次点击的时间 代表已经过了标志时间{flagTime}了-->然后执行回调
                if (currentTime - flagTime > clickTime) {
                    clickTime = System.currentTimeMillis();
                    iMenuClick.onClick(menuItem);
                }
                return false;
            }
        });
    }
}
