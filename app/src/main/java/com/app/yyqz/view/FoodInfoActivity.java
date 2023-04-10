package com.app.yyqz.view;

import static com.app.yyqz.utils.TipsUtil.printError;
import static com.app.yyqz.utils.TipsUtil.showToast;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.yyqz.App;
import com.app.yyqz.R;
import com.app.yyqz.databinding.ActivityFoodInfoBinding;
import com.app.yyqz.network.NetworkPath;
import com.app.yyqz.network.entity.FoodEntity;
import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;

import lombok.val;

public class FoodInfoActivity extends AppCompatActivity {

    // ViewBinding
    private ActivityFoodInfoBinding mBinding;

    // 美食信息
    private FoodEntity food;

    // 红色
    private final ColorStateList mRedColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.red);

    // 灰色
    private final ColorStateList mGreyColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.grey);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化mBinding并且设置视图
        mBinding = ActivityFoodInfoBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 获取上一个Activity传过来的数据
        food = (FoodEntity) getIntent().getExtras().getSerializable("data");

        // 初始化视图
        initView();
    }

    // 需重写 创建菜单项方法并实现 菜单加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // AID是什么？ AID就是 播放bilibili视频的一个链接需要的请求参数，AID代表一个视频的ID，可以这么理解
        // 参考链接如下 https://www.cnblogs.com/wkfvawl/p/12268980.html
        // AID为空 代表为查询到景区相关信息，一般不会存在，除非是网络和后端没有写好
        if (food.getAid() == null || food.getAid().isEmpty()) {
            showToast(this, "请稍等，尚未查询到景区相关信息");
            return true;
        }

        // 如果点击的是视频 则前往播放视频的Activity
        if (item.getItemId() == R.id.menu_video) {
            val intent = new Intent(this, FoodInfoVideoActivity.class);

            // 传入AID
            intent.putExtra("data", food.getAid());

            // 跳转
            startActivity(intent);
        }
        return true;
    }


    private void initView() {

        // 设置工具栏
        setSupportActionBar(mBinding.foodInfoToolBar);

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white)
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // Glide 加载图标 路径+图片全名
        Glide.with(this)
                .load(NetworkPath.FoodImageContainer + food.getImageName())
                .into(mBinding.foodInfoImageView);

        // 设置美食标题
        mBinding.foodInfoTitle.setText(food.getTitle());

        // 设置美食内容描述
        mBinding.foodInfoDesc.setText(food.getDesc());

        // 设置美食评分图片的色调
        setImageViewTint(food.getScore(), mBinding.foodStartImage1, mBinding.foodStartImage2, mBinding.foodStartImage3, mBinding.foodStartImage4, mBinding.foodStartImage5);
    }


    // 设置图片色调 index 代表从左往右每个图片的位置， 比如 第一个图片 1星 、 第二哥图片 2星 ... ImageView 为控件
    private void setImageViewTint(int index, ImageView... imageView) {

        // ImageView的长度不为 5 则退出方法
        if (imageView.length != 5) return;

        // 因为集合元素都是从0开始，所以index - 1
        int pos = index - 1;

        // 循环 5次
        for (int i = 0; i < 5; i++) {


            // 顺序是 评分一到五从左往右
            // 如果 当前点击的位置 小于 i 代表需要将 右边的评分的颜色初始值
            if (pos < i) {
                if (imageView[i].getBackgroundTintList() == mRedColor) {
                    imageView[i].setBackgroundTintList(mGreyColor);
                }
            }
            // 否则 左边的评分颜色 设置红色突出评分
            else {
                if (imageView[i].getBackgroundTintList() == mGreyColor) {
                    imageView[i].setBackgroundTintList(mRedColor);
                }
            }
        }
    }
}