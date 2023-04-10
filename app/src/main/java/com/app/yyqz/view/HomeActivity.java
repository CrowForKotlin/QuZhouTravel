package com.app.yyqz.view;

import static com.app.yyqz.utils.DPUtil.dp2px;
import static com.app.yyqz.utils.GsonUtil.gson;
import static com.app.yyqz.utils.TipsUtil.showToast;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.yyqz.R;
import com.app.yyqz.databinding.ActivityHomeBinding;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.adapter.HomeBannerAdapter;
import com.app.yyqz.adapter.HomeRvAdapter;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.response.RespTourEntity;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.view.dialog.LikeTypeDialog;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;
import com.to.aboomy.pager2banner.IndicatorView;
import com.to.aboomy.pager2banner.ScaleInTransformer;

import java.util.ArrayList;

import lombok.val;

public class HomeActivity extends AppCompatActivity {

    // ViewBinding
    private ActivityHomeBinding mBinding;

    // 主页景点RecyvlerView(Rv)适配器
    private final HomeRvAdapter mAdapter = new HomeRvAdapter(new ArrayList<>());

    // 主页轮播图适配器
    private final HomeBannerAdapter mHomeBannerAdapter = new HomeBannerAdapter(new ArrayList<>());

    private ArrayList<RespTourEntity> respTourEntities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (User.IsGuest) {
            val sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            if (sp.getBoolean("auto", false)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
        mBinding = ActivityHomeBinding.inflate(getLayoutInflater());  // 初始化 mBinding 通过LayoutInflater去加载得到的一个轻量级ViewBinding组件
        setContentView(mBinding.getRoot());
        initView();
        initData();
        initPermission();
    }

    private void initPermission() {

        // 申请权限 网络和访问存储权限
        registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {

        }).launch(new String[]{permission.ACCESS_NETWORK_STATE,
                permission.INTERNET,
                permission.ACCESS_WIFI_STATE,
                permission.CHANGE_WIFI_STATE,
                permission.WRITE_EXTERNAL_STORAGE
        });
    }

    // 需重写 创建菜单项方法并实现 菜单加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed((Runnable) () -> {
            // 如果是游客
            if (User.IsGuest) {
                MenuItem item = mBinding.homeToolBar.getMenu().findItem(R.id.menu_home_usr);
                item.setTitle("登录");
                item.setIcon(null);
                MenuItem itemFood = mBinding.homeToolBar.getMenu().findItem(R.id.menu_home_food);
                itemFood.setVisible(false);
            }
        }, 100L);
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // 点击的是美食Icon则跳转美食Activity
        if (item.getItemId() == R.id.menu_home_food) {
            startActivity(new Intent(this, FoodActivity.class));
        }

        // 点击的是用户Icon则跳转美食Activity
        else if (item.getItemId() == R.id.menu_home_usr) {
            if (!User.IsGuest) {
                val intent = new Intent(this, UserActivity.class);
                val bundle = new Bundle();
                bundle.putSerializable("data", respTourEntities);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
                val sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                val edit = sp.edit();
                edit.putBoolean("exit_by_home", true);
                edit.apply();
                finish();
            }
        }

        return true;

    }

    // 初始化View
    private void initView() {

        // 设置ActionBar
        setSupportActionBar(mBinding.homeToolBar);

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white)
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // 设置轮播图的高度为屏幕像素高度 / 4
        mBinding.homeBody.homeBanner.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels / 4;

        // 第一次加载时刷新
        mBinding.homeBody.homeRefresh.setRefreshing(true);
        mBinding.homeBody.homeRefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.pink));

        // 设置刷新事件
        mBinding.homeBody.homeRefresh.setOnRefreshListener(() -> {
            int size = mAdapter.mRespTourEntities.size();
            mAdapter.mRespTourEntities.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
            getHomePageData();
        });

        // 配置轮播图
        mBinding.homeBody.homeBanner

                // 添加滑动时的缩放效果
                .addPageTransformer(new ScaleInTransformer())

                // 设置轮播图每一页的边距
                .setPageMargin(dp2px(this, 20), dp2px(this, 10))

                // 设置指示器（就是下方的小原点）
                .setIndicator(
                        new IndicatorView(this)

                                // 指示器颜色灰色
                                .setIndicatorColor(Color.DKGRAY)

                                // 指示器的选择器设置蓝色
                                .setIndicatorSelectorColor(ContextCompat.getColor(this, R.color.blue_1fa2ff))

                                // 设置指示器的样式
                                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BEZIER)
                )
                // 设置适配器
                .setAdapter(mHomeBannerAdapter);

        // 设置适配器
        mBinding.homeBody.homeRv.setAdapter(mAdapter);

        // 如果用户的信息没有喜欢的类型 并且不是游客 则提示设置
        if (!User.IsGuest && User.info.getLikeType() == null) {
            new LikeTypeDialog(this, this).show();
        }
    }

    // 初始化数据
    private void initData() {

        // 获取主页数据
        getHomePageData();

        // 获取轮播图 一般来说轮播图的数据是不变的，所以这里就固定从1-4获取需要展示轮播图的图片
        for (int i = 1; i < 5; i++) {
            mHomeBannerAdapter.mImageUrls.add(i + ".jpg");
            mHomeBannerAdapter.notifyItemChanged(mHomeBannerAdapter.mImageUrls.size() - 1);
        }
    }

    private void getHomePageData() {
        // 获取主页数据
        NetWorkFactory.GetHomePage(this, response -> {

            // Gson反序列化Json数据得到 ArrayList<RespTourEntity>
            // 这里的TypeToken构造是为了拿到 ArrayList<RespTourEntity>这个类型，因为Java还没有表示泛型类型的方法，通过Gson库的提供去实现
            respTourEntities = gson.fromJson(response, new TypeToken<ArrayList<RespTourEntity>>() {}.getType());

            // 请求码为 -1 则代表失败 退出逻辑 避免往下执行
            if (respTourEntities.get(0).getCode() == -1) {
                mBinding.homeBody.homeRefresh.setRefreshing(false);
                showToast(this, "请求失败，请重试！");
                return;
            }


            // 拿到数据后循的往适配器中添加并更新
            for (RespTourEntity tour : respTourEntities) {
                mAdapter.mRespTourEntities.add(tour);
                mAdapter.notifyItemChanged(mAdapter.mRespTourEntities.size() - 1);
            }
            mBinding.homeBody.homeRefresh.setRefreshing(false);
        });
    }
}