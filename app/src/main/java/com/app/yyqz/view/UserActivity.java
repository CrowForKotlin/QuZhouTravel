package com.app.yyqz.view;

import static com.app.yyqz.utils.GsonUtil.gson;
import static com.app.yyqz.utils.TipsUtil.showToast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.app.yyqz.R;
import com.app.yyqz.adapter.HomeRvAdapter;
import com.app.yyqz.adapter.UserRvAdapter;
import com.app.yyqz.databinding.ActivityUserBinding;
import com.app.yyqz.databinding.HomeBodyBinding;
import com.app.yyqz.databinding.StartsLayoutBinding;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.network.response.RespBookingTicketsEntity;
import com.app.yyqz.network.response.RespStartTourEntity;
import com.app.yyqz.network.response.RespTourEntity;
import com.app.yyqz.network.response.UserTicketsDatas;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.utils.ViewUtils;
import com.app.yyqz.view.dialog.CancelBookingDialog;
import com.app.yyqz.view.dialog.LikeTypeDialog;
import com.app.yyqz.view.dialog.UserChangeDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.reflect.TypeToken;
import com.gyf.immersionbar.ImmersionBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import lombok.val;


public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding mBinding;
    private AlertDialog dialog;
    private HomeRvAdapter tourRvAdapter =  new HomeRvAdapter(new ArrayList<>());;
    private ArrayList<RespTourEntity> respTourEntities;
    private Boolean getTourOkay = true;
    private final UserRvAdapter mUserRvAdaoter = new UserRvAdapter(new ArrayList<>(), datas -> {
        new CancelBookingDialog(UserActivity.this, UserActivity.this, new CancelBookingDialog.ICancelTypeCallback() {
            @Override
            public void doOnCancelAll() {

                // 取消预约全部门票
                doCancelBookingTickets(datas);
            }

            @Override
            public void doOnCancelCount(int value) {

                // 如果取消的门票数量 超过 当前已预约的门票数量，那么就toast提示
                if (datas.getBookingTicketsCount() < value) {
                    TipsUtil.showLongToast(UserActivity.this, "取消预约的门票数超过总共的预约的门票数！");
                    return;
                }

                // 门票数不能为 0
                else if (value == 0) {
                    TipsUtil.showLongToast(UserActivity.this, "取消预定的门票不能为0！");
                    return;
                }

                // 取消预约门票
                doCancelBookingTickets(new UserTicketsDatas(value, datas.getTourName(), datas.getTourImageName(), datas.getDate()));
            }
        }).show();
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        respTourEntities = (ArrayList<RespTourEntity>) getIntent().getExtras().getSerializable("data");
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private void initView() {

        // 沉浸式效果
        ImmersionBar.with(this)
                .statusBarDarkFont(true)                // 状态栏图标暗色
                .navigationBarDarkIcon(true)            // 导航栏图标暗色
                .navigationBarColor(R.color.white)
                .init();                                // 应用以上配置

        // 获取状态栏的高度， 并给 当前布局设置 和顶部状态的间距 从而实现沉浸式效果
        mBinding.getRoot().setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);

        // 设置Title
        mBinding.userToolbar.setTitle(getString(R.string.Welcom, User.info.getName()));

        // 设置用户主页 已预约门票的适配器
        mBinding.userRv.setAdapter(mUserRvAdaoter);

        // 修改昵称
        ViewUtils.clickGap(mBinding.userChangeUsr, ViewUtils.GAP_TIME, view -> new UserChangeDialog(this, this, UserChangeDialog.UserChangeType.NAME, (text) -> mBinding.userToolbar.setTitle(getString(R.string.Welcom, text))).show());

        // 修改密码
        ViewUtils.clickGap(mBinding.userChangePwd, ViewUtils.GAP_TIME, view -> new UserChangeDialog(this, this, UserChangeDialog.UserChangeType.PWD, (text) -> {
        }).show());

        // 修改喜欢的口味
        ViewUtils.clickGap(mBinding.userChangeLikeType, ViewUtils.GAP_TIME, view -> new LikeTypeDialog(this, this).show());

        // 用户退出
        ViewUtils.clickGap(mBinding.userExit, ViewUtils.GAP_TIME, view -> {
            val sp = getSharedPreferences("login", Context.MODE_PRIVATE);
            val edit = sp.edit();
            edit.putBoolean("exit_by_user", true);
            edit.apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // 修改喜欢的口味
        ViewUtils.clickGap(mBinding.userStarts, ViewUtils.GAP_TIME, view -> {
            showStarts();
        });

    }

    private void showStarts() {
        if (!getTourOkay) {
            showToast(this, "请稍等...正在获取收藏的景点");
            return;
        }
        getTourOkay = false;
        val binding = HomeBodyBinding.bind(LayoutInflater.from(this).inflate(R.layout.home_body, mBinding.getRoot(), false));
        val dialog = new BottomSheetDialog(this);
        dialog.setContentView(binding.getRoot());
        binding.homeRv.setAdapter(tourRvAdapter);
        tourRvAdapter.mRespTourEntities.clear();
        tourRvAdapter.notifyDataSetChanged();
        NetWorkFactory.GetTourStart(this, User.info.getUsername(), response -> {
            ArrayList<RespStartTourEntity> datas = gson.fromJson(response, new TypeToken<ArrayList<RespStartTourEntity>>() {}.getType());
            if (datas.isEmpty()) {
                showToast(this, "暂无收藏的景点！");
                return;
            }
            for (RespTourEntity entity : respTourEntities) {
                for (RespStartTourEntity startTour : datas) {
                    if (entity.getName().equals(startTour.getName()) && startTour.getIsStart()) {
                        tourRvAdapter.mRespTourEntities.add(entity);
                    }
                }
            }
            tourRvAdapter.notifyDataSetChanged();
            dialog.show();
            getTourOkay = true;
        });
    }

    private void initData() {
        doGetBookingTickets();
    }

    private void doGetBookingTickets() {

        // 获取用户预定的门票
        NetWorkFactory.GetPreBookingTickets(this, User.info.getUsername(), response -> {
            RespBookingTicketsEntity datas = gson.fromJson(response, RespBookingTicketsEntity.class);
            if (datas.getCode() == -1) {

                // 代表门票不存在 显示 ”暂无预定的门票“
                mBinding.userRv.setVisibility(View.GONE);
                mBinding.userNoTickets.setVisibility(View.VISIBLE);
            } else {

                // 门票数据存在 就更新数据
                for (UserTicketsDatas userTicketsData : datas.getUserTicketsDatas()) {
                    mUserRvAdaoter.mUserTicketsData.add(userTicketsData);
                    mUserRvAdaoter.notifyItemChanged(mUserRvAdaoter.mUserTicketsData.size() - 1);
                }
            }
        });
    }

    // 删除预约门票
    private void doCancelBookingTickets(UserTicketsDatas data) {

        // 用户取消以及预定的门票
        NetWorkFactory.CancelBookingTickets(this, User.info.getUsername(), data.getTourName(), String.valueOf(data.getBookingTicketsCount()), response -> {
            int code = Integer.parseInt(response);
            if (code == -2) {
                showToast(this, "请求失败，请重试");
                return;
            }
            showToast(this, "取消预约成功！");
            if (code == -1) {

                // -1代表 门票已经从用户的数据中完全删除了
                val index = mUserRvAdaoter.mUserTicketsData.indexOf(data);
                mUserRvAdaoter.mUserTicketsData.remove(index);
                mUserRvAdaoter.notifyItemRemoved(index);
            } else {

                // 否则 门票数据存在、但个数需要更新个数
                for (int pos = 0; pos < mUserRvAdaoter.mUserTicketsData.size(); pos++) {
                    // 获取适配器的数据
                    val ticketsDatas = mUserRvAdaoter.mUserTicketsData.get(pos);

                    // 如果适配器门票数据的景点名 和 日期完全匹配
                    if (Objects.equals(ticketsDatas.getDate(), data.getDate()) && ticketsDatas.getTourName().equals(data.getTourName())) {

                        // 设置适配器预定门票数据 个数
                        ticketsDatas.setBookingTicketsCount(ticketsDatas.getBookingTicketsCount() - data.getBookingTicketsCount());

                        // 通知适配器位置 pos 的项目发生改变
                        mUserRvAdaoter.notifyItemChanged(pos);
                        break;
                    }
                }
            }

            // 设置不可见 并显示 文本 ”暂无预定的门票“
            if (mUserRvAdaoter.mUserTicketsData.isEmpty()) {
                mBinding.userRv.setVisibility(View.GONE);
                mBinding.userNoTickets.setVisibility(View.VISIBLE);
            }
        });
    }
}