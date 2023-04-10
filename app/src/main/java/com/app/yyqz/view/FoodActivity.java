package com.app.yyqz.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.yyqz.R;
import com.app.yyqz.adapter.FoodAdapter;
import com.app.yyqz.databinding.ActivityFoodBinding;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.entity.FoodEntity;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.network.response.RespFoodEntity;
import com.app.yyqz.utils.GsonUtil;
import com.app.yyqz.view.dialog.EditTypeDialog;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity {

    private ActivityFoodBinding mBinding;
    private final FoodAdapter mAdapter = new FoodAdapter(this, new ArrayList<>());
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        sharedPreferences = getSharedPreferences("isLikeType", Context.MODE_PRIVATE);
        initView();
        initData();
    }

    // 需重写 创建菜单项方法并实现 菜单加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // 筛选按钮
        if (item.getItemId() == R.id.menu_food_edit) {
            boolean likeType = sharedPreferences.getBoolean("isLikeType", false);

            // 筛选窗体
            new EditTypeDialog(this, this, likeType, new EditTypeDialog.IEditTypeCallback() {
                @Override
                public void doOnLikeType() {

                    // 保存选择喜欢的类型： 是
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("isLikeType", true);
                    edit.apply();

                    // 获取美食大小
                    int size = mAdapter.mFoods.size();

                    // 清除美食大小
                    mAdapter.mFoods.clear();

                    // 通知RecyclerView移除一个范围的项目
                    mAdapter.notifyItemRangeRemoved(0, size);

                    // 获取喜欢的美食类型
                    doGetFoodHomeDataByLikeType();
                }

                @Override
                public void doOnRecommend() {
                    // 保存选择喜欢的类型： 否
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putBoolean("isLikeType", false);
                    edit.apply();

                    // 获取美食大小
                    int size = mAdapter.mFoods.size();

                    // 清除
                    mAdapter.mFoods.clear();

                    // 通知RecyclerView移除一个范围的项目
                    mAdapter.notifyItemRangeRemoved(0, size);

                    // 获取根据评分推荐的美食
                    doGetFoodHomeDataByRecommend();
                }
            }).show();
        }
        return true;
    }

    private void initData() {

        // 根据保存的数值 是否是喜欢的类型
        if (!sharedPreferences.getBoolean("isLikeType", false)) {
            doGetFoodHomeDataByRecommend();
        } else {
            doGetFoodHomeDataByLikeType();
        }
    }

    private void initView() {

        // 设置ActionBar
        setSupportActionBar(mBinding.foodToolBar);

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white)
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        mBinding.foodRv.setAdapter(mAdapter);
    }

    public void doGetFoodHomeDataByRecommend() {
        NetWorkFactory.getFoodHomePageByRecommend(this, User.info.getUsername(), response -> {
            RespFoodEntity respFoodEntity = GsonUtil.gson.fromJson(response, RespFoodEntity.class);
            ArrayList<FoodEntity> foodEntities = respFoodEntity.getFoodEntities();
            for (FoodEntity food : foodEntities) {
                mAdapter.mFoods.add(food);
                mAdapter.notifyItemChanged(mAdapter.mFoods.size() - 1);
            }
        });
    }

    public void doGetFoodHomeDataByLikeType() {
        NetWorkFactory.getFoodHomePageByLikeType(this, User.info.getUsername(), response -> {
            RespFoodEntity respFoodEntity = GsonUtil.gson.fromJson(response, RespFoodEntity.class);
            ArrayList<FoodEntity> foodEntities = respFoodEntity.getFoodEntities();
            for (FoodEntity food : foodEntities) {
                mAdapter.mFoods.add(food);
                mAdapter.notifyItemChanged(mAdapter.mFoods.size() - 1);
            }
        });
    }
}