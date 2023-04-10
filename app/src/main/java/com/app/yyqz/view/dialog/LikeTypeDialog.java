package com.app.yyqz.view.dialog;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.app.yyqz.databinding.FoodChipBinding;
import com.app.yyqz.network.NetWorkFactory;
import com.app.yyqz.network.entity.User;
import com.app.yyqz.utils.TipsUtil;
import com.app.yyqz.utils.ViewUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class LikeTypeDialog {

    public Activity mActivity;
    public Context mContext;

    public LikeTypeDialog(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    public void show() {
        FoodChipBinding binding = FoodChipBinding.inflate(mActivity.getLayoutInflater());
        AlertDialog alertDialog = new MaterialAlertDialogBuilder(mContext)
                .setTitle("选择您喜欢的美食口味")
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();
        alertDialog.show();
        ViewUtils.clickGap(binding.foodChipSubmit, ViewUtils.GAP_TIME, view1 -> {
            StringBuilder builder = new StringBuilder();
            if (binding.chipKu.isChecked()) {
                builder.append("0");
            }
            if (binding.chipLa.isChecked()) {
                builder.append(",1");
            }
            if (binding.chipTian.isChecked()) {
                builder.append(",2");
            }
            if (binding.chipXian.isChecked()) {
                builder.append(",3");
            }
            String likeType = builder.toString();
            if (!likeType.isEmpty()) {
                if (likeType.startsWith(",")) {
                    likeType = likeType.substring(1);
                }
                NetWorkFactory.ChangeLikeType(mActivity, User.info.getUsername(), likeType , response -> {
                    var code = Integer.parseInt(response);
                    if (code == -1) {
                        TipsUtil.showToast(mContext, "请求失败，请重试！");
                        alertDialog.dismiss();
                        return;
                    }
                    alertDialog.dismiss();
                    TipsUtil.showToast(mContext, "设置成功！");
                });
            } else {
                TipsUtil.showToast(mContext, "请选择喜欢的口味！");
            }
        });
    }

}
