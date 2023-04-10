package com.app.yyqz.view;

import static com.app.yyqz.utils.TipsUtil.showToast;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.yyqz.App;
import com.app.yyqz.R;
import com.app.yyqz.adapter.TourTrafficRvAdapter;
import com.app.yyqz.databinding.ActivityTourBinding;
import com.app.yyqz.network.BaiduFactory;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.NetworkPath;
import com.app.yyqz.network.entity.BaiduSearchPlaceEntity;
import com.app.yyqz.network.entity.BaiduTrafficScopeEntity;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.network.response.RespTourEntity;
import com.app.yyqz.utils.GsonUtil;
import com.app.yyqz.utils.ViewUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import lombok.val;

public class TourActivity extends AppCompatActivity {

    // 定义一个下载回调的接口
    @FunctionalInterface
    public interface IDownLoad {
        void onSuccess();
    }

    // ViewBinding
    private ActivityTourBinding mBinding;

    // 交通状况Rv适配器
    private final TourTrafficRvAdapter mAdapter = new TourTrafficRvAdapter(new ArrayList<>(), null, null);

    // 红色
    private final ColorStateList mRedColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.red);

    // 黑色
    private final ColorStateList mBlackColor = ContextCompat.getColorStateList(App.getContextObject(), R.color.black);

    // 景点信息
    private RespTourEntity respTourEntity;

    // 景点MapUID ： 打开百度地图的一个 ID值， 需要查询地方获取得到
    private String mMapUid = null;

    // 音频播放器
    private MediaPlayer mMediaPlayer = null;

    // 声音文件
    private File mSoundFile = null;


    private MaterialDatePicker<Long> mDatePicker = null;

    private Boolean isStartNow = false;

    // 是否收藏景区
    private Boolean isStartTour = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化ViewBinding
        mBinding = ActivityTourBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // 获取景点数据
        Bundle bundle = getIntent().getExtras();
        respTourEntity = (RespTourEntity) bundle.getSerializable("data");

        initView();
         initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果Activity执行onPause，判断播放器是否正在播放，然后暂停播放释放资源
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    // 需重写 创建菜单项方法并实现 菜单加载
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tour, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // 如果mMapUid为空 代表找不到景区相关信息
        if (mMapUid == null) {
            showToast(this, "请稍等，尚未查询到景区相关信息");
            return true;
        }

        // 点击百度地图Icon就传入mMapUid 然后跳转
        if (item.getItemId() == R.id.menu_tour_map) {
            val intent = new Intent(this, BaiduMapActivity.class);
            intent.putExtra("data", mMapUid);
            startActivity(intent);
        }
        return true;
    }

    private void initData() {
        // 这里是调用百度地图Web接口
        // 1：显获取景区附近的相关地方， 为了获取UID
        BaiduFactory.GetPlace(this, respTourEntity.getName(), respTourEntity.getCity(), response -> {

            // 反序列化成一个Java对象
            val placeEntity = GsonUtil.gson.fromJson(response, BaiduSearchPlaceEntity.class);

            // 获取结果如果不为空
            if (!placeEntity.getResult().isEmpty()) {

                // 通过resultDTO.getLocation().getUid() 并赋值就得到了mMapUid
                val resultDTO = placeEntity.getResult().get(0);
                val locationDTO = resultDTO.getLocation();

                mMapUid = resultDTO.getUid();

                // 得到mMapUid后 继续获取景区的经纬度 x、y轴位置 传入 即可
                BaiduFactory.GetTrafficScope(this, locationDTO.getLat().toString(), locationDTO.getLng().toString(), response1 -> {

                    // 获取到景区2公里内的公路道路的情况 是否通畅、拥堵
                    val trafficScopeEntity = GsonUtil.gson.fromJson(response1, BaiduTrafficScopeEntity.class);

                    // 获取完整的是否通畅的信息 比如 “该区域整体通畅”
                    mAdapter.mCompleteDesc = trafficScopeEntity.getDescription();

                    // 获取状态信息 比如 “通畅”
                    val evaluation = trafficScopeEntity.getEvaluation();
                    if (evaluation != null) {
                        mAdapter.mStatusDesc = evaluation.getStatusDesc();
                    }

                    if (trafficScopeEntity.getRoadTraffic() != null) {

                        // 还会得到一个道路列表 循环每一个道路
                        for (BaiduTrafficScopeEntity.RoadTrafficDTO trafficDTO : trafficScopeEntity.getRoadTraffic()) {

                            // 获取道路名称 如果是UNKNOW代表没有查询到或者未知的道路名
                            if (!trafficDTO.getRoadName().equals("UNKNOW")) {

                                // 添加 不为UNKNOW的道路名称
                                mAdapter.mRoadNames.add(trafficDTO.getRoadName());

                                // 通知RecyclerView适配器只听位置的项目发生改变
                                mAdapter.notifyItemChanged(mAdapter.mRoadNames.size() - 1);
                            }
                        }

                    }

                    // 如果Recycler道路名称列表为空， 代表没有任何数据
                    if (mAdapter.mRoadNames.isEmpty()) {

                        // 设置状态空， 不显示
                        mAdapter.mStatusDesc = "";

                        // 设置道路名称为 ”未查询到附近的道路“
                        mAdapter.mRoadNames.add("未查询到附近道路");

                        // 通知第0个项目改变
                        mAdapter.notifyItemChanged(0);
                    }
                });
            }
        });

        if (!User.IsGuest) {
            NetWorkFactory.GetTourIsStart(this, User.info.getUsername(),respTourEntity.getName(), response -> {
                int code = Integer.parseInt(response);
                if (code == 1) {
                    mBinding.tourStart.setIconTint(mRedColor);
                    isStartTour = true;
                }
                isStartNow = false;
            });
        }
    }

    private void initView() {

        // 设置工具栏
        setSupportActionBar(mBinding.tourToolBar);

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white)
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);


        // 加载网络图片 （图片路径 + 图片名）
        Glide.with(this)
                .load(NetworkPath.ImageContainer + respTourEntity.getImageName())
                .into(mBinding.tourImageview);

        // 设置景点相关信息 ...
        mBinding.tourTitle.setText(respTourEntity.getName());
        mBinding.tourLevel.setText(respTourEntity.getLevel());
        mBinding.tourLocate.setText(respTourEntity.getLocate());
        mBinding.tourOpenTime.setText(respTourEntity.getOpenTime());
        mBinding.tourDesc.setText(respTourEntity.getDesc());
        mBinding.tourTickets.setText(getString(R.string.Tickets, respTourEntity.getTickets()));
        mBinding.tourTicketsCount.setText("0");

        // 票数 + 1
        mBinding.tourPlus.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tourTicketsCount.getText().toString());
            if (count > 4) {
                showToast(this, "最多预约五张门票奥！");
                return;
            }
            mBinding.tourTicketsCount.setText(String.valueOf(++count));
        });

        // 票数 - 1
        mBinding.tourMinus.setOnClickListener(v -> {
            int count = Integer.parseInt(mBinding.tourTicketsCount.getText().toString());
            if (count < 1) return;
            mBinding.tourTicketsCount.setText(String.valueOf(--count));
        });

        // 预定门票 按钮点击
        ViewUtils.clickGap(mBinding.tourBook, ViewUtils.GAP_TIME, view -> {
            String ticketsCount = mBinding.tourTicketsCount.getText().toString();
            if (Integer.parseInt(ticketsCount) < 1) {
                showToast(this, "预定的票数不能为 0");
                return;
            }

            mDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("选择预约的日期").build();
            mDatePicker.show(getSupportFragmentManager(), null);
            mDatePicker.addOnPositiveButtonClickListener(selection -> {
                if (selection < System.currentTimeMillis()) {
                    showToast(this, "预约的日期不能小于当天");
                    return;
                }
                val format = App.simpleDateFormat.format(new Date(selection));
                doBookingTickets(ticketsCount, respTourEntity.getName(), format,respTourEntity.getImageName());
            });

        });

        // 设置交通状况适配器
        mBinding.tourRvTraffic.setAdapter(mAdapter);

        // 获取两个Drawable off和on的图标
        Drawable mSouncOff = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_sound_off_24dp);
        Drawable mSouncOn = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_sound_on_24dp);

        // 声音按钮 点击
        ViewUtils.clickGap(mBinding.tourSound, ViewUtils.GAP_TIME, view -> {

            // 执行下载的回调
            doOnDownLoad(() -> {

                // 初始化音频播放器
                if (mMediaPlayer == null) {
                    mMediaPlayer = MediaPlayer.create(this, Uri.fromFile(mSoundFile));
                }

                // 正在播放则暂停并从0开始 设置 on的图标
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mMediaPlayer.seekTo(0);
                    mBinding.tourSound.setIcon(mSouncOn);
                }

                // 否则 重新播放
                else {
                    mMediaPlayer.start();
                    mBinding.tourSound.setIcon(mSouncOff);
                }
            });
        });

        ViewUtils.clickGap(mBinding.tourStart, ViewUtils.GAP_TIME, view -> {
            doStartTour(respTourEntity.getName(), !isStartTour);
            isStartTour = !isStartTour;
        });
    }

    private void doOnDownLoad(IDownLoad iDownLoad) {

        // 获取声音文件
        mSoundFile = new File(getExternalCacheDir(), respTourEntity.getName() + ".mp3");

        // 如果 下载的文件大小不为0，代表下载成功 执行回调成功，并退出方法避免继续下载
        if (mSoundFile.length() != 0) {
            iDownLoad.onSuccess();
            return;
        }

        // 下载MP3  app 目录下有个叫做cache的文件夹下面
        NetWorkFactory.DownloadMp3(this, respTourEntity.getName() + ".mp3", response -> {

            // 输入流
            InputStream inputStream;

            // 文件输出流
            FileOutputStream outputStream;
            try {
                // 先读后写 然后关闭
                inputStream = response.byteStream();
                outputStream = new FileOutputStream(mSoundFile);
                int len;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
                inputStream.close();
                outputStream.close();

                // 主线程执行回调，因为网络请求不在主线程中执行
                runOnUiThread(iDownLoad::onSuccess);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void doStartTour(String tourName, Boolean isStart) {
        if (User.IsGuest) {
            showToast(this, "请登录后才可使用此功能");
            return;
        }

        if (isStartNow) {
            showToast(this, "等一下，正在收藏中噢...");
            return;
        }
        isStartNow = true;

        NetWorkFactory.StartTour(this, User.info.getUsername(), tourName, isStart, response -> {
            int code = Integer.parseInt(response);
            if (code == 0) {
                if (isStart) {
                    showToast(this, "收藏成功");
                    mBinding.tourStart.setIconTint(mRedColor);
                } else {
                    showToast(this, "取消收藏成功");
                    mBinding.tourStart.setIconTint(mBlackColor);
                }
                isStartNow = false;
                return;
            }
            isStartNow = false;
            showToast(this, "收藏失败");
        });
    }

    private void doBookingTickets(String ticketsCount, String tourName, String date, String tourImageName) {

        if (User.IsGuest) {
            showToast(this, "登录后才可使用此功能！");
            return;
        }

        // 发送预定门票的请求
        NetWorkFactory.BookTickets(this, User.info.getUsername(), ticketsCount, tourName, tourImageName, date, response -> {
            int code = Integer.parseInt(response);
            if (code == -1) {
                showToast(this, "您已经预定过这个景点的门票了！");
                return;
            } else if (code == -2) {
                showToast(this, "预定的门票数量不够了!");
                return;
            } else if (code == -3) {
                showToast(this, "订票失败");
                return;
            }
            showToast(this, "订票成功, 在主界面滑动刷新可更新数据！");
            if (mDatePicker != null) {
                if (mDatePicker.isVisible()) mDatePicker.dismiss();
            }
        });
    }
}